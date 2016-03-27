package me.drakeet.mailotto.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.RxMail;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onSend(View view) {
        RxMail.getInstance().send(new Mail(ConsumerActivity.class, "A mail from MainActivity"));
    }


    public void onStartConsumer(View view) {
        startActivity(new Intent(this, ConsumerActivity.class));
    }
}
