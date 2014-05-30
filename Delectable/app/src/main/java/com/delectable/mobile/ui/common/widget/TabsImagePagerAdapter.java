package com.delectable.mobile.ui.common.widget;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TabsImagePagerAdapter extends FragmentStatePagerAdapter implements
        SlidingPagerTabStrip.ImageTabProvider {

    private ArrayList<Fragment> mFragments;

    private ArrayList<Integer> mTabButtonImageResIds;

    public TabsImagePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,
            ArrayList<Integer> tabButtonImageResIds) {
        super(fm);
        mFragments = fragments;
        mTabButtonImageResIds = tabButtonImageResIds;
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
    public int getPageImageResId(int position) {
        return mTabButtonImageResIds.get(position);
    }
}
