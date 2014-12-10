package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingTabAdapter;
import com.delectable.mobile.ui.common.widget.SlidingTabLayout;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFragment extends BaseFragment {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private SlidingTabAdapter mTabsAdapter;

    @InjectView(R.id.tab_layout)
    protected SlidingTabLayout mTabLayout;

    @InjectView(R.id.pager)
    protected ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up tab icons and fragments
        SlidingTabAdapter.SlidingTabItem wines = new SlidingTabAdapter.SlidingTabItem(
                new SearchWinesTabFragment(),
                getString(R.string.search_wines));
        SlidingTabAdapter.SlidingTabItem people = new SlidingTabAdapter.SlidingTabItem(
                new SearchPeopleTabFragment(),
                getString(R.string.search_people));

        ArrayList<SlidingTabAdapter.SlidingTabItem>
                tabItems = new ArrayList<SlidingTabAdapter.SlidingTabItem>();
        tabItems.add(wines);
        tabItems.add(people);

        mTabsAdapter = new SlidingTabAdapter(getFragmentManager(), tabItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, rootView);

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.d_off_white));
        mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.d_chestnut));

        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setViewPager(mViewPager);

        return rootView;
    }
}
