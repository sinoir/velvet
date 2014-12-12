package com.delectable.mobile.ui.common.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class SlidingTabAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = SlidingTabAdapter.class.getSimpleName();

    private List<SlidingTabItem> mTabs;

    public SlidingTabAdapter(FragmentManager fm, List<SlidingTabAdapter.SlidingTabItem> tabs) {
        super(fm);
        mTabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return mTabs.get(position).mFragment;
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).mTabTitle;
    }

    public boolean hasUpdates(int position) {
        return mTabs.get(position).mHasUpdates;
    }

    public static class SlidingTabItem {

        Fragment mFragment;

        String mTabTitle;

        boolean mHasUpdates;

        public SlidingTabItem(Fragment fragment, String tabTitle, boolean hasUpdates) {
            mFragment = fragment;
            mTabTitle = tabTitle;
            mHasUpdates = hasUpdates;
        }

        public SlidingTabItem(Fragment fragment, String tabTitle) {
            this(fragment, tabTitle, false);
        }
    }
}