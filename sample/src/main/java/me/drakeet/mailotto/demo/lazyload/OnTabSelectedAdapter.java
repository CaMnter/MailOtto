package me.drakeet.mailotto.demo.lazyload;

import android.support.design.widget.TabLayout;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 16/4/19 23:57
 */
public abstract class OnTabSelectedAdapter implements TabLayout.OnTabSelectedListener {

    @Override public abstract void onTabSelected(TabLayout.Tab tab);

    @Override public void onTabUnselected(TabLayout.Tab tab) {

    }


    @Override public void onTabReselected(TabLayout.Tab tab) {

    }
}
