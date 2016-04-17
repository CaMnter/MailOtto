package me.drakeet.mailotto.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.Mailbox;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
    }


    public void onPreload(final View view) {
        mHandler.postDelayed(new Runnable() {
            @Override public void run() {
                ((TextView) view).append(": done!");
                Mailbox.getInstance()
                       .post(new Mail("A mail from MainActivity", TargetActivity.class));
            }
        }, 8 * 1000);
    }


    public void onStartNextPage(View view) {
        startActivity(NextActivity.getIntent(this, /*page = */2));
        Toast.makeText(this, "MainActivity has been destroyed.", Toast.LENGTH_LONG).show();
        this.finish();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }
}
