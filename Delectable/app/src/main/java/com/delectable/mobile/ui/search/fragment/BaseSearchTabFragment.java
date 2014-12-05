package com.delectable.mobile.ui.search.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This base class handles the SearchView in the ActionBar, it's text listener, and the layout
 * view.
 */
public abstract class BaseSearchTabFragment extends BaseFragment
        implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final String TAG = BaseSearchTabFragment.class.getSimpleName();

    protected SearchView mSearchView;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    @InjectView(R.id.empty_view_search)
    protected FontTextView mEmptyStateTextView;

    protected abstract BaseAdapter getAdapter();

    protected String mCurrentQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setHasOptionsMenu(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Close Nav drawer if opened:
        if (item.getItemId() == mSearchView.getId()) {
            closeNavigationDrawer();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        RelativeLayout layout = (RelativeLayout) inflater
                .inflate(R.layout.fragment_search_wines_people, container, false);
        ButterKnife.inject(this, layout);

        mListView.setAdapter(getAdapter());
        mListView.setOnItemClickListener(this);
        mListView.setEmptyView(mEmptyStateTextView);

        return layout;
    }

    /**
     * Subclasses should call super on this method to ensure that the keyboard gets hidden when a
     * query is made.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurrentQuery = query;
        mSearchView.clearFocus(); //hides keyboard
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSearchView != null) {
            mSearchView.clearFocus(); //hides keyboard
        }
    }
}
