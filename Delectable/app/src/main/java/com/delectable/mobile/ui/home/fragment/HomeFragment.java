package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingPagerAdapter;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;

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

public class HomeFragment extends BaseFragment {

    private View mView;

    private ViewPager mViewPager;

    private SlidingPagerTabStrip mTabStrip;

    private SlidingPagerAdapter mTabsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        String currentUserId = UserInfo.getUserId(getActivity());

        mView = inflater.inflate(R.layout.fragment_viewpager_with_tabstrip, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTabStrip = (SlidingPagerTabStrip) mView.findViewById(R.id.tabstrip);

        ArrayList<SlidingPagerAdapter.SlidingPagerItem> tabItems
                = new ArrayList<SlidingPagerAdapter.SlidingPagerItem>();

        // "FOLLOWING" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                FollowerFeedTabFragment.newInstance(currentUserId),
                R.color.d_off_white,
                R.color.d_chestnut,
                R.color.dark_gray_to_chestnut,
                getString(R.string.home_tab_following)));

        // "YOU" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                TrendingTabFragment.newInstance(currentUserId),
                R.color.d_off_white,
                R.color.d_chestnut,
                R.color.dark_gray_to_chestnut,
                getString(R.string.home_tab_trending)));

        mTabsAdapter = new SlidingPagerAdapter(getFragmentManager(), tabItems);

        mViewPager.setAdapter(mTabsAdapter);
        mTabStrip.setViewPager(mViewPager);

        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getBaseActivity().deregisterHeaderView(mTabStrip);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().registerHeaderView(mTabStrip);
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
