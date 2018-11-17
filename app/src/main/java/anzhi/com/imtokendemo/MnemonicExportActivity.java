package anzhi.com.imtokendemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MnemonicExportActivity extends AppCompatActivity {

    @BindView(R.id.mnemonic_tv) TextView mnemonicTextView;
    @BindView(R.id.backup_btn) Button backupBtn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnemonic_export);

        context = this;
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String ethereumMnemonic = intent.getStringExtra("EthereumMnemonic");
        String bitcoinMnemonic = intent.getStringExtra("BitcoinMnemonic");
        mnemonicTextView.setText("助记词是：" + ethereumMnemonic);

        initBackupBtn();
    }

    private void initBackupBtn() {
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
