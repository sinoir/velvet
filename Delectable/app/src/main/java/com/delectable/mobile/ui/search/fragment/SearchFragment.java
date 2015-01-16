package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingTabAdapter;
import com.delectable.mobile.ui.common.widget.SlidingTabLayout;
import com.delectable.mobile.ui.search.widget.SearchToolbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A custom toolbar searchview, {@link SearchToolbar} is used here, because the searchview provided
 * by the v7 support library is not pretty like the searches inside the google apps.
 */
public class SearchFragment extends BaseFragment implements SearchView.OnQueryTextListener {

    private static final String TAG = SearchFragment.class.getSimpleName();

    //# pages to the left/right for the viewpager to retain
    private static final int PAGES_LIMIT = 2;

    /**
     * Search starts automatically while typing after this many milliseconds
     */
    private static final int AUTO_SEARCH_DELAY = 300;

    private SlidingTabAdapter mTabsAdapter;

    @InjectView(R.id.tab_layout)
    protected SlidingTabLayout mTabLayout;

    @InjectView(R.id.pager)
    protected ViewPager mViewPager;

    private String mCurrentQuery;

    private HashSet<SearchView.OnQueryTextListener> mListeners
            = new HashSet<SearchView.OnQueryTextListener>();

    private Handler mAutoSearchHandler = new Handler();

    private Runnable mAutoSearchTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up tab icons and fragments
        SlidingTabAdapter.SlidingTabItem wines = new SlidingTabAdapter.SlidingTabItem(
                new SearchWinesTabFragment(),
                getString(R.string.search_wines).toLowerCase());
        SlidingTabAdapter.SlidingTabItem hashtags = new SlidingTabAdapter.SlidingTabItem(
                new SearchHashtagsTabFragment(),
                getString(R.string.search_hashtags).toLowerCase());
        SlidingTabAdapter.SlidingTabItem people = new SlidingTabAdapter.SlidingTabItem(
                new SearchPeopleTabFragment(),
                getString(R.string.search_people).toLowerCase());

        ArrayList<SlidingTabAdapter.SlidingTabItem>
                tabItems = new ArrayList<SlidingTabAdapter.SlidingTabItem>();
        tabItems.add(wines);
        tabItems.add(hashtags);
        tabItems.add(people);

        mTabsAdapter = new SlidingTabAdapter(getChildFragmentManager(), tabItems);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);

            //custom searchview
            getActionBar().setCustomView(R.layout.toolbar_search_impl);
            getActionBar().setDisplayShowCustomEnabled(true);

            SearchToolbar searchToolbar = (SearchToolbar) getActionBar().getCustomView();
            searchToolbar.setOnQueryTextListener(this);
            searchToolbar.showKeyboard();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, rootView);

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
        mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));

        mViewPager.setAdapter(mTabsAdapter);
        mViewPager.setOffscreenPageLimit(PAGES_LIMIT);
        mTabLayout.setViewPager(mViewPager);

        return rootView;
    }

    @Override
    public boolean onQueryTextChange(final String s) {
        for (SearchView.OnQueryTextListener listener : mListeners) {
            listener.onQueryTextChange(s);
        }
        mAutoSearchHandler.removeCallbacks(mAutoSearchTask);
        mAutoSearchTask = new Runnable() {
            @Override
            public void run() {
                onQueryTextSubmit(s);
            }
        };
        mAutoSearchHandler.postDelayed(mAutoSearchTask, AUTO_SEARCH_DELAY);
        return false;
    }

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
