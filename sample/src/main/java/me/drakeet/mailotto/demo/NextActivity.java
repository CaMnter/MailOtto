package me.drakeet.mailotto.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    private static final String EXTRA_PAGE = "extra_page";
    private int mPage;


    public static Intent getIntent(Context context, int page) {
        Intent intent = new Intent(context, NextActivity.class);
        intent.putExtra(EXTRA_PAGE, page);
        return intent;
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        mPage = getIntent().getIntExtra(EXTRA_PAGE, 2);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(Integer.toString(mPage));
    }


    public void onStartNextPage(View view) {
        if (mPage == 3) {
            startActivity(TargetActivity.getIntent(this));
        } else {
            startActivity(getIntent(this, ++mPage));
        }
        this.finish();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
    }
}

