package me.drakeet.mailotto.demo.preload;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.Mailbox;
import me.drakeet.mailotto.OnMailReceived;
import me.drakeet.mailotto.demo.R;
import me.drakeet.mailotto.demo.lazyload.LazyActivity;

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


    @OnMailReceived public void onPreloadDataReady(Mail mail) {
        mTextView.setText(mail.content.toString());
        View gotoLazyLoad = findViewById(R.id.goto_lazy_load);
        gotoLazyLoad.setVisibility(View.VISIBLE);
        gotoLazyLoad.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(LazyActivity.newIntent(TargetActivity.this));
            }
        });
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        Mailbox.getInstance().leave(this);
    }
}
