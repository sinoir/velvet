package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;
import com.delectable.mobile.ui.common.widget.TabsImagePagerAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private View mView;

    private ViewPager mViewPager;

    private SlidingPagerTabStrip mTabStrip;

    private TabsImagePagerAdapter mTabsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTabStrip = (SlidingPagerTabStrip) mView.findViewById(R.id.tabstrip);

        ArrayList<Integer> imageId = new ArrayList<Integer>();
        ArrayList<Fragment> tabFragments = new ArrayList<Fragment>();

        imageId.add(R.drawable.ab_feed);
        tabFragments.add(FollowFeedTabFragment.newInstance());

        imageId.add(R.drawable.ab_profile);
        tabFragments.add(UserProfileTabFragment.newInstance());

        imageId.add(R.drawable.ab_activity);
        tabFragments.add(ActivityFeedTabFragment.newInstance());

        mTabsAdapter = new TabsImagePagerAdapter(getFragmentManager(), tabFragments, imageId);

        mViewPager.setAdapter(mTabsAdapter);
        mTabStrip.setViewPager(mViewPager);

        return mView;
    }
}
