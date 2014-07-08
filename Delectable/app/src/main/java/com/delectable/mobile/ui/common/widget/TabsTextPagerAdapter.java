package com.delectable.mobile.ui.common.widget;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TabsTextPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragments;

    private ArrayList<String> mTabButtonTitles;

    public TabsTextPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,
            ArrayList<String> tabButtonTitles) {
        super(fm);
        mFragments = fragments;
        mTabButtonTitles = tabButtonTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTabButtonTitles != null && mTabButtonTitles.size() == mFragments.size()) {
            return mTabButtonTitles.get(position);
        }
        return "";
    }
}