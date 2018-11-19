package anzhi.com.imtokendemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.consenlabs.tokencore.wallet.Identity;
import org.consenlabs.tokencore.wallet.Wallet;
import org.consenlabs.tokencore.wallet.WalletManager;
import org.consenlabs.tokencore.wallet.model.Metadata;
import org.consenlabs.tokencore.wallet.model.MnemonicAndPath;
import org.consenlabs.tokencore.wallet.model.Network;

import anzhi.com.util.AZStringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateIdentityActivity extends AppCompatActivity {
    private static final String TAG = "CreateIdentityActivity";

    @BindView(R.id.identity_name) EditText identityNameEditText;
    @BindView(R.id.passwd) EditText passwdEditText;
    @BindView(R.id.confirm_passwd) EditText confirmPasswdEditText;
    @BindView(R.id.hint) EditText hintEditText;
    @BindView(R.id.createBtn) Button createBtn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_identity);

        context = this;
        ButterKnife.bind(this);

        initCreateBtn();
    }

    private void initCreateBtn() {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAvailabe = checkAvailableValue();
                if (isAvailabe) {
                    String identityName = identityNameEditText.getText().toString();
                    String passwd = passwdEditText.getText().toString();
                    String hint = hintEditText.getText().toString();
                    Identity identity = Identity.createIdentity(identityName, passwd, hint, Network.MAINNET, Metadata.P2WPKH);
                    if (null != identity) {
                        Wallet ethereumWallet = identity.getWallets().get(0);
                        Wallet bitcoinWallet = identity.getWallets().get(1);
                        if (null != ethereumWallet) {
                            Toast.makeText(context, "ETH钱包地址是" + ethereumWallet.getAddress(), Toast.LENGTH_LONG).show();
                            Log.i(TAG, "eth address is: " + ethereumWallet.getAddress());
                            Log.i(TAG, "btc address is: " + bitcoinWallet.getAddress());
                            String ethereumId = ethereumWallet.getId();
                            String bitcoinId = bitcoinWallet.getId();
                            MnemonicAndPath ethereumMnemonic = WalletManager.exportMnemonic(ethereumId, passwd);
                            MnemonicAndPath bitcoinMnemonic = WalletManager.exportMnemonic(bitcoinId, passwd);
                            Log.i(TAG, "eth mnemonic is: " + ethereumMnemonic.getMnemonic());
                            Log.i(TAG, "btc mnemonic is: " + bitcoinMnemonic.getMnemonic());
                            Intent intent = new Intent(context, MnemonicExportActivity.class);
                            intent.putExtra("EthereumMnemonic", ethereumMnemonic.getMnemonic());
                            intent.putExtra("BitcoinMnemonic", bitcoinMnemonic.getMnemonic());
                            identity.addWallet(ethereumWallet);
                            identity.addWallet(bitcoinWallet);
                            SharedPreferences.Editor editor = getSharedPreferences("ids", MODE_PRIVATE).edit();
                            editor.putString("EthId", ethereumWallet.getId());
                            editor.putString("BtcId", bitcoinWallet.getId());
                            editor.commit();
                            context.startActivity(intent);
                        } else {
                            Log.i(TAG, "获取ETH钱包失败");
                        }
                    } else {
                        Toast.makeText(context, "创建钱包失败", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "创建钱包失败");
                    }
                } else {
                    Toast.makeText(context, "参数不合法", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "参数不合法");
                }
            }
        });
    }

    private boolean checkAvailableValue() {
        // TODO: 更精确的限制策略
        String identityName = identityNameEditText.getText().toString();
        String passwd = passwdEditText.getText().toString();
        String confirmPasswd = confirmPasswdEditText.getText().toString();
        String hint = hintEditText.getText().toString();
        if (AZStringUtil.isTrimEmpty(identityName) || AZStringUtil.isTrimEmpty(passwd) || AZStringUtil.isTrimEmpty(hint)) {
            return false;
        }
        if (passwd.length() < 6) {
            return false;
        }
        if (!passwd.equals(confirmPasswd)) {
            return false;
        }
        return true;
    }

}
