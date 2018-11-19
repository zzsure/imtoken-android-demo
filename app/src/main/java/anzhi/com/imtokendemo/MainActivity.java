package anzhi.com.imtokendemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.consenlabs.tokencore.wallet.KeystoreStorage;
import org.consenlabs.tokencore.wallet.WalletManager;

import java.io.File;

import anzhi.com.util.AZStringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements KeystoreStorage {

    @BindView(R.id.create_identity) Button createIdentityBtn;
    @BindView(R.id.eth_layout) LinearLayout ethLayout;
    @BindView(R.id.btc_layout) LinearLayout btcLayout;

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
            }
        }
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
}
