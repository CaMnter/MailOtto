package me.drakeet.mailotto.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import me.drakeet.mailotto.Mailbox;
import me.drakeet.mailotto.demo.bean.ResponseData;
import me.drakeet.mailotto.mail.PreloadMail;

public class PreloadMailActivity extends AppCompatActivity {

    private Handler mHandler;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        mHandler = new Handler();
    }


    public void onPreload(final View view) {
        ((TextView) view).append(": loading...");
        mHandler.postDelayed(new InnerRunnable(view), 8 * 1000);
    }


    public void onStartNextPage(View view) {
        startActivity(NextActivity.getIntent(this, /*page = */2));
        Toast.makeText(this, "MainActivity has been destroyed.", Toast.LENGTH_LONG).show();
        this.finish();
    }


    public static class InnerRunnable implements Runnable {

        WeakReference<TextView> textViewPreference;


        public InnerRunnable(View textView) {
            this.textViewPreference = new WeakReference<>((TextView) textView);
        }


        @Override public void run() {
            if (textViewPreference.get() != null) {
                textViewPreference.get().append(": done!");
            }

            // TODO Network request success ...
            /*
             * 返回网络请求成功后的数据
             * 假设为：ResponseData
             * 这里的话 new 一个 ResponseData 模拟请求成功
             */
            final ResponseData responseData = new ResponseData();
            responseData.data = "PreloadMail response data";
            Mailbox.getInstance().post(new PreloadMail<ResponseData>(TargetActivity.class) {
                @Override public ResponseData packaging() {
                    return responseData;
                }
            });
        }
    }
}
