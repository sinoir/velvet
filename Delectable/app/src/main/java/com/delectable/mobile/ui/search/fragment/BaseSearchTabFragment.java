package com.delectable.mobile.ui.search.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;

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

    @InjectView(R.id.empty_state_text_view)
    protected FontTextView mEmptyStateTextView;

    protected abstract BaseAdapter getAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: Custom Back Arrow...
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setOnQueryTextListener(this);

        //programmatically change search close icon, unable to change searchView attributes via xml styles
        int closeButtonId = mSearchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) mSearchView.findViewById(closeButtonId);
        closeButton.setImageResource(R.drawable.btn_ab_close_search);
        //TODO platform bug with setIconifiedByDefault, makes searchview look wierd
        //searchView.setIconifiedByDefault(false);

        if (!getAdapter().isEmpty()) {
            mSearchView.clearFocus();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        RelativeLayout layout = (RelativeLayout) inflater
                .inflate(R.layout.fragment_search_wines_people, container, false);
        ButterKnife.inject(this, layout);

        mListView.setAdapter(getAdapter());
        mListView.setOnItemClickListener(this);

        //TODO make better one, no designs for this empty state
        mListView.setEmptyView(mEmptyStateTextView);
        return layout;
    }

    /**
     * Subclasses should call super on this method to ensure that the keyboard gets hidden when a
     * query is made.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus(); //hides keyboard
        return false;
    }


}
