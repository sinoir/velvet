package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingPagerAdapter;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;
import com.delectable.mobile.ui.followfriends.fragment.FollowContactsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowExpertsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowFacebookFriendsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowTwitterFriendsTabFragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFragment extends BaseFragment implements SearchView.OnQueryTextListener{

    private static final String TAG = SearchFragment.class.getSimpleName();

    private SlidingPagerAdapter mTabsAdapter;


    @InjectView(R.id.tabstrip)
    protected SlidingPagerTabStrip mTabStrip;

    @InjectView(R.id.pager)
    protected ViewPager mViewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //set up tab icons and fragments
        SlidingPagerAdapter.SlidingPagerItem wines = new SlidingPagerAdapter.SlidingPagerItem(
                new SearchWinesTabFragment(),
                R.color.d_off_white,
                R.color.d_light_green,
                R.color.medium_gray_to_dark_gray,
                getString(R.string.search_wines));
        SlidingPagerAdapter.SlidingPagerItem people = new SlidingPagerAdapter.SlidingPagerItem(
                new SearchPeopleTabFragment(),
                R.color.d_off_white,
                R.color.d_light_green,
                R.color.medium_gray_to_dark_gray,
                getString(R.string.search_people));

        ArrayList<SlidingPagerAdapter.SlidingPagerItem>
                tabItems = new ArrayList<SlidingPagerAdapter.SlidingPagerItem>();
        tabItems.add(wines);
        tabItems.add(people);

        mTabsAdapter = new SlidingPagerAdapter(getFragmentManager(), tabItems);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: Custom Back Arrow...
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        //TODO platform bug with setIconifiedByDefault, makes searchview look wierd
        //searchView.setIconifiedByDefault(false);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, rootView);

        mViewPager.setAdapter(mTabsAdapter);
        mTabStrip.setViewPager(mViewPager);

        return rootView;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG + ".onQueryTextSubmit", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG + ".onQueryTextChange", newText);
        return false;
    }
}
