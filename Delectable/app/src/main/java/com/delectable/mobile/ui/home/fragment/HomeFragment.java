package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;
import com.delectable.mobile.ui.common.widget.TabsImagePagerAdapter;
import com.delectable.mobile.ui.registration.activity.LoginActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private View mView;

    private ViewPager mViewPager;

    private SlidingPagerTabStrip mTabStrip;

    private TabsImagePagerAdapter mTabsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.capture_wine:
                Toast.makeText(getActivity(), "Launch Capture Wine!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sign_out:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        UserInfo.onSignOut(getActivity());
        Intent launchIntent = new Intent();
        launchIntent.setClass(getActivity(), LoginActivity.class);
        startActivity(launchIntent);
        getActivity().finish();
    }
}
