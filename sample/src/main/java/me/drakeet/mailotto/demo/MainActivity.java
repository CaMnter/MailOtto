package me.drakeet.mailotto.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.Mailbox;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onSend(View view) {
        Mailbox.getInstance().send(new Mail("A mail from MainActivity", ConsumerActivity.class));
    }


    public void onStartConsumer(View view) {
        startActivity(new Intent(this, ConsumerActivity.class));
    }
}
