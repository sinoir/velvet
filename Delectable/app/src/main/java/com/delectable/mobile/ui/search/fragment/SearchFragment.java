package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingTabAdapter;
import com.delectable.mobile.ui.common.widget.SlidingTabLayout;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFragment extends BaseFragment implements SearchView.OnQueryTextListener {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private SearchView mSearchView;

    private SlidingTabAdapter mTabsAdapter;

    @InjectView(R.id.tab_layout)
    protected SlidingTabLayout mTabLayout;

    @InjectView(R.id.pager)
    protected ViewPager mViewPager;

    private String mCurrentQuery;

    private HashSet<SearchView.OnQueryTextListener> mListeners
            = new HashSet<SearchView.OnQueryTextListener>();

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

        mTabsAdapter = new SlidingTabAdapter(getChildFragmentManager(), tabItems);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);

            //TODO in order to make searh experience better (like google drive), will have to make custom searchview instead of crappy v7 one
            //getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            //getActionBar().setCustomView(R.layout.toolbar_searchview);
            //mSearchView = (SearchView) getActionBar().getCustomView().findViewById(R.id.search_view);
            //mSearchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, rootView);

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
        mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.d_white));

        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setViewPager(mViewPager);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        mSearchView.setOnQueryTextListener(this);

        mSearchView.setIconified(false);

        if (mCurrentQuery != null && !mCurrentQuery.isEmpty()) {
            mSearchView.setQuery(mCurrentQuery, false);
        }
    }

    public SearchView getSearchView() {
        return mSearchView;
    }


    @Override
    public boolean onQueryTextChange(String s) {
        for (SearchView.OnQueryTextListener listener : mListeners) {
            listener.onQueryTextChange(s);
        }
        return false;
    }


    /**
     * Subclasses should call super on this method to ensure that the keyboard gets hidden when a
     * query is made.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurrentQuery = query;
        for (SearchView.OnQueryTextListener listener : mListeners) {
            listener.onQueryTextSubmit(query);
        }
        return false;
    }

    public void registerSearchListener(SearchView.OnQueryTextListener listener) {
        mListeners.add(listener);
    }

    public void deregisterSearchListener(SearchView.OnQueryTextListener listener) {
        mListeners.remove(listener);
    }

}
