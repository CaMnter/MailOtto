package me.drakeet.mailotto.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.Mailbox;
import me.drakeet.mailotto.OnMailReceived;

public class ConsumerActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);
        Mailbox.getInstance().atHome(this);
    }


    public void onSend(View view) {
        // A mail send to self.
        Mailbox.getInstance()
               .post(new Mail("A mail send to self", this.getClass(), this.getClass()));
    }


    @OnMailReceived public void onDearMailReceived(Mail mail) {
        Toast.makeText(ConsumerActivity.this, mail.content.toString(), Toast.LENGTH_SHORT).show();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
    }
}

