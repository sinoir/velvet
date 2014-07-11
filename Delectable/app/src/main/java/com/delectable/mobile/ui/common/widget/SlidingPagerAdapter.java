package com.delectable.mobile.ui.common.widget;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SlidingPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<SlidingPagerItem> mData;

    public SlidingPagerAdapter(FragmentManager fm, ArrayList<SlidingPagerItem> data) {
        super(fm);
        mData = data;
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position).mFragment;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position).mTabTitle;
    }

    public int getPageIcon(int position) {
        return mData.get(position).mTabIcon;
    }

    public int getBackgroundColor(int position) {
        return mData.get(position).mBackgroundColor;
    }

    public int getTabTitleColor(int position) {
        return mData.get(position).mTabTitleTextColor;
    }

    public int getTabIndicatorColor(int position) {
        return mData.get(position).mIndicatorColor;
    }

    public static class SlidingPagerItem {

        /**
         * Create customzied Tab Item -> Ideally most color resources have states
         *
         * @param fragment          - Fragment Item to be displayed for tab
         * @param backgroundColor   - Background drawable/color resource id
         * @param indicatorColor    - Indicator color resource id
         * @param tabTitleTextColor - Title color resource id
         * @param tabTitle          - Tab Title String
         */
        public SlidingPagerItem(Fragment fragment, int backgroundColor, int indicatorColor,
                int tabTitleTextColor,
                String tabTitle) {
            this(fragment, backgroundColor, indicatorColor, tabTitleTextColor, tabTitle, 0);
        }

        /**
         * Create customzied Tab Item -> Ideally most color resources have states
         *
         * @param fragment        - Fragment Item to be displayed for tab
         * @param backgroundColor - Background drawable/color resource id
         * @param indicatorColor  - Indicator color resource id
         * @param tabIcon         - Tab icon - Must be used if tab title is not used
         */
        public SlidingPagerItem(Fragment fragment, int backgroundColor, int indicatorColor,
                int tabIcon) {
            this(fragment, backgroundColor, indicatorColor, 0, null, tabIcon);
        }

        /**
         * Create customzied Tab Item -> Ideally most color resources have states
         *
         * @param fragment          - Fragment Item to be displayed for tab
         * @param backgroundColor   - Background drawable/color resource id
         * @param indicatorColor    - Indicator color resource id
         * @param tabTitleTextColor - Title color resource id
         * @param tabTitle          - Tab Title String - Must be used if tab icon is not used
         * @param tabIcon           - Tab icon - Must be used if tab title is not used
         */
        public SlidingPagerItem(Fragment fragment, int backgroundColor, int indicatorColor,
                int tabTitleTextColor,
                String tabTitle, int tabIcon) {
            mFragment = fragment;
            mBackgroundColor = backgroundColor;
            mIndicatorColor = indicatorColor;
            mTabTitleTextColor = tabTitleTextColor;
            mTabTitle = tabTitle;
            mTabIcon = tabIcon;
        }

        Fragment mFragment;

        int mBackgroundColor;

        int mIndicatorColor;

        int mTabTitleTextColor;

        String mTabTitle;

        int mTabIcon;
    }
}