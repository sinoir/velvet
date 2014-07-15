package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.camera.activity.WineCaptureActivity;
import com.delectable.mobile.ui.common.widget.SlidingPagerAdapter;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;
import com.delectable.mobile.ui.registration.activity.LoginActivity;
import com.facebook.Session;

import android.content.Intent;
import android.os.Bundle;
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

        mView = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTabStrip = (SlidingPagerTabStrip) mView.findViewById(R.id.tabstrip);

        ArrayList<SlidingPagerAdapter.SlidingPagerItem> tabItems
                = new ArrayList<SlidingPagerAdapter.SlidingPagerItem>();

        // "FOLLOWING" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                FollowFeedTabFragment.newInstance(),
                R.color.d_dark_navy,
                R.color.d_light_green,
                R.color.tab_text_white_grey,
                getString(R.string.home_tab_following)));

        // "YOU" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                FollowFeedTabFragment.newInstance(currentUserId),
                R.color.d_dark_navy,
                R.color.d_light_green,
                R.color.tab_text_white_grey,
                getString(R.string.home_tab_you)));

        mTabsAdapter = new SlidingPagerAdapter(getFragmentManager(), tabItems);

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
                launchWineCapture();
                return true;
            case R.id.sign_out:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void launchWineCapture() {
        Intent launchIntent = new Intent();
        launchIntent.setClass(getActivity(), WineCaptureActivity.class);
        startActivity(launchIntent);
    }

    public void logout() {
        // TODO: Some Common Logout function
        Session session = Session.getActiveSession();
        if (session != null) {
            session.closeAndClearTokenInformation();
        }
        UserInfo.onSignOut(getActivity());
        Intent launchIntent = new Intent();
        launchIntent.setClass(getActivity(), LoginActivity.class);
        startActivity(launchIntent);
        getActivity().finish();
    }
}
