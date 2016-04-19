package me.drakeet.mailotto.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.Mailbox;
import me.drakeet.mailotto.OnMailReceived;
import me.drakeet.mailotto.demo.bean.ResponseData;
import me.drakeet.mailotto.mail.PreloadMail;

public class TargetActivity extends AppCompatActivity {

    TextView mTextView;


    public static Intent getIntent(Context context) {
        return new Intent(context, TargetActivity.class);
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        mTextView = (TextView) findViewById(R.id.text);
        Mailbox.getInstance().atHome(this);
    }


    @SuppressWarnings("unchecked") @OnMailReceived public void onPreloadDataReady(Mail mail) {
        if (mail instanceof PreloadMail) {
            PreloadMail<ResponseData> preloadMail = (PreloadMail<ResponseData>) mail;
            mTextView.setText(preloadMail.preData.data);
        } else {
            mTextView.setText(mail.content.toString());
        }
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        Mailbox.getInstance().leave(this);
    }
}
