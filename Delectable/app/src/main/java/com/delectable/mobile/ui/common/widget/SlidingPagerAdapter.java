package com.delectable.mobile.ui.common.widget;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class SlidingPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = SlidingPagerAdapter.class.getSimpleName();

    private final FragmentManager mFragmentManager;

    private ArrayList<SlidingPagerItem> mData;

    public SlidingPagerAdapter(FragmentManager fm, ArrayList<SlidingPagerItem> data) {
        super(fm);
        mFragmentManager = fm;
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

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
        // Need to sync the mFragments list from whithin FragmentStatePagerAdapter to get the right references for each fragment
        // This is almost the same code to the super restoreState() method
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            Iterable<String> keys = bundle.keySet();
            ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>();
            for (String key : keys) {
                if (key.startsWith("f")) {
                    int index = Integer.parseInt(key.substring(1));
                    Fragment f = mFragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        while (fragmentsList.size() <= index) {
                            fragmentsList.add(null);
                        }
                        f.setMenuVisibility(false);
                        fragmentsList.set(index, f);
                    } else {
                        Log.w(TAG, "Bad fragment at key " + key);
                    }
                }
            }
            // Sync the SlidingPagerItem list with current fragmentList:
            if (mData != null && mData.size() == fragmentsList.size()) {
                for (int i = 0; i < mData.size(); i++) {
                    mData.get(i).mFragment = fragmentsList.get(i);
                }
            }
        }
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

        Fragment mFragment;

        int mBackgroundColor;

        int mIndicatorColor;

        int mTabTitleTextColor;

        String mTabTitle;

        int mTabIcon;

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
    }
}