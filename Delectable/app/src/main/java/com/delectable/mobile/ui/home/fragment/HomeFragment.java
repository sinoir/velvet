package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingTabAdapter;
import com.delectable.mobile.ui.common.widget.SlidingTabLayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private View mView;

    private ViewPager mViewPager;

    private SlidingTabLayout mTabLayout;

    private SlidingTabAdapter mTabsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        String currentUserId = UserInfo.getUserId(getActivity());

        mView = inflater.inflate(R.layout.fragment_viewpager_with_sliding_tabs, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTabLayout = (SlidingTabLayout) mView.findViewById(R.id.tab_layout);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.d_off_white));
        mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.d_chestnut));

        List<SlidingTabAdapter.SlidingTabItem> tabItems
                = new ArrayList<SlidingTabAdapter.SlidingTabItem>();

        // "FOLLOWING" tab
        tabItems.add(new SlidingTabAdapter.SlidingTabItem(
                FollowerFeedTabFragment.newInstance(currentUserId),
                getString(R.string.home_tab_following),
                false));

        // "TRENDING" tab
        tabItems.add(new SlidingTabAdapter.SlidingTabItem(
                TrendingTabFragment.newInstance(currentUserId),
                getString(R.string.home_tab_trending),
                true));

        mTabsAdapter = new SlidingTabAdapter(getFragmentManager(), tabItems);
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setViewPager(mViewPager);

        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getBaseActivity().deregisterHeaderView(mTabLayout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().registerHeaderView(mTabLayout);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: Search in Menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
