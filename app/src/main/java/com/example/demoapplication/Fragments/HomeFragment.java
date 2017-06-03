package com.example.demoapplication.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demoapplication.R;
import com.example.demoapplication.UtilClass.SmartFragmentStatePagerAdapter;
import com.example.demoapplication.Widget.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anshultyagi on 02/06/17.
 */

public class HomeFragment extends Fragment {
    public static String fragments[];
    View rootView;
    TabLayout mainTabLayout;
    NonSwipeableViewPager viewPager;
    private ViewPagerAdapter mPagerAdapter;

    public void changeLayoutManager() {
        Fragment fragment = mPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
        if (fragment instanceof CompletedTaskFragment) {
            ((CompletedTaskFragment) fragment).changeLayoutManager();
        } else if (fragment instanceof PendingTaskFragment) {
            ((PendingTaskFragment) fragment).changeLayoutManager();
        }
    }

    public void onRefresh() {
        Fragment fragment = mPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
        if (fragment instanceof CompletedTaskFragment) {
            ((CompletedTaskFragment) fragment).prepareMovieData();
        } else if (fragment instanceof PendingTaskFragment) {
            ((PendingTaskFragment) fragment).prepareMovieData();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        mainTabLayout = (TabLayout) rootView.findViewById(R.id.mainTabLayout);
        viewPager = (NonSwipeableViewPager) rootView.findViewById(R.id.viewPager);
        //Enable/Disable Swipe
        viewPager.setPagingEnabled(false);

        fragments = new String[]{"Completed \nTask", "Pending \nTask"};
        mPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        mainTabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            View customView = mPagerAdapter.getCustomView(getActivity(), i);
            mainTabLayout.getTabAt(i).setCustomView(customView);
        }

        mainTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (viewPager != null) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                Fragment fragment = mPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
                if (fragment instanceof CompletedTaskFragment) {
                    ((CompletedTaskFragment) fragment).prepareMovieData();
                    ((CompletedTaskFragment)fragment).changeLayoutManager();
                } else if (fragment instanceof PendingTaskFragment) {
                    ((PendingTaskFragment) fragment).prepareMovieData();
                    ((PendingTaskFragment)fragment).changeLayoutManager();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }

    public static class ViewPagerAdapter extends SmartFragmentStatePagerAdapter {
        private List<Map<String, Object>> maps = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getCustomView(Context context, int pos) {
            View mView = LayoutInflater.from(context).inflate(R.layout.custom_tab_view, null);
            TextView mTextView = (TextView) mView.findViewById(R.id.textView);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTypeface(Typeface.createFromAsset(context.getAssets(),
                    "fonts/aller_regular.ttf"));
            mTextView.setText(getPageTitle(pos));
            return mView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CompletedTaskFragment();
                case 1:
                    return new PendingTaskFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
