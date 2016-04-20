package me.drakeet.mailotto.demo.lazyload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import me.drakeet.mailotto.Mail;
import me.drakeet.mailotto.Mailbox;
import me.drakeet.mailotto.OnMailReceived;
import me.drakeet.mailotto.demo.R;

public class LazyActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private PlaceholderFragment[] mFragments = { PlaceholderFragment.newInstance(0),
            PlaceholderFragment.newInstance(1), LazyLoadFragment.newInstance(-999) };


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LazyActivity.class);
        return intent;
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new OnPageChangeAdapter() {
            int lastPosition = 0;


            @Override public void onPageSelected(int newPosition) {
                ((LifecycleCompat) mSectionsPagerAdapter.getItem(newPosition)).onResumeCompat();
                ((LifecycleCompat) mSectionsPagerAdapter.getItem(lastPosition)).onPauseCompat();
                lastPosition = newPosition;
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new OnTabSelectedAdapter() {

            @Override public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), false);
            }
        });

        Mailbox.getInstance().post(new Mail("Go!", LazyLoadFragment.class));
    }


    public static class LazyLoadFragment extends PlaceholderFragment {

        public LazyLoadFragment() {
        }


        public static LazyLoadFragment newInstance(int sectionNumber) {
            LazyLoadFragment fragment = new LazyLoadFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override public void onResumeCompat() {
            super.onResumeCompat();
            Mailbox.getInstance().atHome(this);
        }


        @Override public void onPause() {
            super.onPause();
            Mailbox.getInstance().leave(this);
        }


        @OnMailReceived public void onLazyLoad(Mail mail) {
            Toast.makeText(getContext(), "Load your views and data now...", Toast.LENGTH_LONG)
                 .show();
        }
    }

    public static class PlaceholderFragment extends Fragment implements LifecycleCompat {

        protected static final String ARG_SECTION_NUMBER = "section_number";


        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_lazy, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(
                    getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }


        @Override public void onResumeCompat() {

        }


        @Override public void onPauseCompat() {

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override public Fragment getItem(int position) {
            return mFragments[position];
        }


        @Override public int getCount() {
            return mFragments.length;
        }


        @Override public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "LAZY SECTION";
            }
            return null;
        }
    }
}
