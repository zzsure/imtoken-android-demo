package anzhi.com.imtokendemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.consenlabs.tokencore.wallet.KeystoreStorage;
import org.consenlabs.tokencore.wallet.WalletManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements KeystoreStorage {

    @BindView(R.id.create_identity)
    Button createIdentityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        WalletManager.storage = this;
        WalletManager.scanWallets();

        initCreateIdentityBtn();
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
