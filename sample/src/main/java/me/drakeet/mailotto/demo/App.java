package me.drakeet.mailotto.demo;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 16/4/17 16:07
 */
public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
