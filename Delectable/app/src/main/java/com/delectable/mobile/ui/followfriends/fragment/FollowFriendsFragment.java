package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingPagerAdapter;
import com.delectable.mobile.ui.common.widget.SlidingPagerAdapter.SlidingPagerItem;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FollowFriendsFragment extends BaseFragment {

    @InjectView(R.id.pager)
    protected ViewPager mViewPager;

    @InjectView(R.id.tabstrip)
    protected SlidingPagerTabStrip mTabStrip;

    private SlidingPagerAdapter mTabsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up tab icons and fragments
        SlidingPagerItem experts = new SlidingPagerItem(
                new FollowExpertsTabFragment(),
                R.color.d_off_white,
                R.color.d_red_experts_tab,
                R.drawable.tab_follow_friends_experts_icon);
        SlidingPagerItem contacts = new SlidingPagerItem(
                new FollowContactsTabFragment(),
                R.color.d_off_white,
                R.color.d_yellow_contacts_tab,
                R.drawable.tab_follow_friends_contacts_icon);
        SlidingPagerItem facebook = new SlidingPagerItem(
                new FollowFacebookFriendsTabFragment(),
                R.color.d_off_white,
                R.color.d_blue_facebook_tab,
                R.drawable.tab_follow_friends_facebook_icon);
        SlidingPagerItem twitter = new SlidingPagerItem(
                new FollowTwitterFriendsTabFragment(),
                R.color.d_off_white,
                R.color.d_blue_twitter_tab,
                R.drawable.tab_follow_friends_twitter_icon);

        ArrayList<SlidingPagerItem> tabItems = new ArrayList<SlidingPagerItem>();
        tabItems.add(experts);
        tabItems.add(contacts);
        tabItems.add(facebook);
        tabItems.add(twitter);

        mTabsAdapter = new SlidingPagerAdapter(getFragmentManager(), tabItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viewpager_with_tabstrip, container, false);
        ButterKnife.inject(this, view);

        mViewPager.setAdapter(mTabsAdapter);
        mTabStrip.setViewPager(mViewPager);
        mTabStrip.setBackgroundColor(getResources().getColor(R.color.d_off_white));
        return view;
    }
}
