package me.drakeet.mailotto.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.Mailbox;
import rx.Subscription;
import rx.functions.Action1;

public class ConsumerActivity extends AppCompatActivity {

    Subscription mSubscription;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);
        mSubscription = Mailbox.getInstance().toObserverable().subscribe(new Action1<Object>() {
            @Override public void call(Object event) {

                if (event instanceof Mail) {
                    Toast.makeText(ConsumerActivity.this, ((Mail) event).content.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        Mailbox.getInstance().checkMails(this);
    }


    public void onSend(View view) {
        Mailbox.getInstance()
               .send(new Mail("A mail send to self", this.getClass(), this.getClass()));
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }
}

