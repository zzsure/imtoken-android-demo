package anzhi.com.imtokendemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.consenlabs.tokencore.wallet.KeystoreStorage;
import org.consenlabs.tokencore.wallet.Wallet;
import org.consenlabs.tokencore.wallet.WalletManager;

import java.io.File;

import anzhi.com.http.IWalletHttp;
import anzhi.com.http.response.EthBalanceModel;
import anzhi.com.http.response.PongModel;
import anzhi.com.util.AZStringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements KeystoreStorage {
    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.create_identity) Button createIdentityBtn;
    @BindView(R.id.eth_layout) LinearLayout ethLayout;
    @BindView(R.id.btc_layout) LinearLayout btcLayout;
    @BindView(R.id.tv_eth_address) TextView ethAddressTextView;
    @BindView(R.id.tv_eth_balance) TextView ethBalanceTextView;
    @BindView(R.id.tv_btc_address) TextView btcAddressTextView;
    @BindView(R.id.tv_btc_balance) TextView btcBalanceTextView;

    private Wallet btcWallet;
    private Wallet ethWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        WalletManager.storage = this;
        WalletManager.scanWallets();
//        WalletManager.mustFindWalletById("");

        initCreateIdentityBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUi();
    }

    private void updateUi() {
        SharedPreferences read = getSharedPreferences("ids", MODE_PRIVATE);
        if (null != read) {
            String ethId = read.getString("EthId", "");
            String btcId = read.getString("BtcId", "");
            if (!AZStringUtil.isTrimEmpty(ethId) && !AZStringUtil.isTrimEmpty(btcId)) {
                ethLayout.setVisibility(View.VISIBLE);
                btcLayout.setVisibility(View.VISIBLE);
                createIdentityBtn.setVisibility(View.GONE);
                updateWallet(ethId, btcId);
            }
        }
    }

    private void updateWallet(String ethId, String btcId) {
        ethWallet = WalletManager.mustFindWalletById(ethId);
        btcWallet = WalletManager.mustFindWalletById(btcId);
        String btcAddress = btcWallet.getAddress();
        String ethAddress = ethWallet.getAddress();
        btcAddressTextView.setText("地址：" + btcAddress);
        ethAddressTextView.setText("地址：" + ethAddress);
        ethAddress = "0x" + ethAddress;
        updateEthBalance(ethAddress);
    }

    private void initCreateIdentityBtn() {
        createIdentityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateIdentityActivity.class);
                startActivity(intent);
            }
        });
    }

    public File getKeystoreDir() {
        return this.getFilesDir();
    }

    private void updateBtcBalance(String address) {
    }

    private void updateEthBalance(String address) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.etherscan.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IWalletHttp walletService = retrofit.create(IWalletHttp.class);
        Call<EthBalanceModel> call = walletService.getEthBalance("account", "balance", address, "latest");
        call.enqueue(new Callback<EthBalanceModel>() {
            @Override
            public void onResponse(Call<EthBalanceModel> call, Response<EthBalanceModel> response) {
                Log.v(TAG, "url is :" + call.request().url());
                String balance = response.body().result;
                if (!AZStringUtil.isTrimEmpty(balance)) {
                    double balanceDouble = Double.parseDouble(balance) / 1000000000000000000.0f;
                    ethBalanceTextView.setText("余额：" + Double.toString(balanceDouble));
                } else {
                    ethBalanceTextView.setText("余额：0");
                }
            }

            @Override
            public void onFailure(Call<EthBalanceModel> call, Throwable t) {
                ethBalanceTextView.setText("余额获取失败，请重试！");
            }
        });
    }
}
