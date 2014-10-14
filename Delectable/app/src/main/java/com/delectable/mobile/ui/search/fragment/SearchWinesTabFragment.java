package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.controllers.BaseWineController;
import com.delectable.mobile.events.basewines.SearchWinesEvent;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.search.widget.WineSearchAdapter;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

public class SearchWinesTabFragment extends BaseSearchTabFragment implements InfiniteScrollAdapter.ActionsHandler{

    private static final String TAG = SearchWinesTabFragment.class.getSimpleName();

    private WineSearchAdapter mAdapter = new WineSearchAdapter(this);

    @Inject
    protected BaseWineController mBaseWinesController;

    //number of items we fetch at a time
    private final int LIMIT = 20; //TODO 20 items per fetch/more?

    private String mCurrentQuery;

    private boolean mLoadingNextPage = false;

    /**
     * If zero is returned for the current search query, then we know we've reached the end of the
     * list. Should disable infinite scroll. Gets reset with each new search.
     */
    private boolean mEndOfList = false;

    @Override
    protected BaseAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        super.onQueryTextSubmit(query);
        mEndOfList = false; //new search query, reset this flag
        mAdapter.getItems().clear(); //clear current data set
        mCurrentQuery = query;
        mBaseWinesController.searchWine(query, 0, LIMIT);
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onEventMainThread(SearchWinesEvent event) {
        mProgressBar.setVisibility(View.GONE);
        if (event.isSuccessful()) {

            ArrayList<SearchHit<BaseWineMinimal>> hits = event.getResult().getHits();
            if (hits.size() == 0) {
                mEndOfList = true;
            }

            mAdapter.getItems().addAll(hits);
            mAdapter.notifyDataSetChanged();
            mEmptyStateTextView.setText(getResources().getString(R.string.empty_search_wines));
        } else {
            showToastError(event.getErrorMessage());
            mEmptyStateTextView.setText(event.getErrorMessage()); //TODO no empty state designs yet
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseWineMinimal baseWine = mAdapter.getItem(position);
        launchWineProfile(baseWine);
    }

    private void launchWineProfile(BaseWineMinimal baseWine) {
        Intent intent = WineProfileActivity.newIntent(getActivity(), baseWine);
        startActivity(intent);
    }

    @Override
    public void shouldLoadNextPage() {
        if (mEndOfList) {
            return; //end of list, don't infinite scroll
        }
        if (mLoadingNextPage) {
            return; //current loading next page, don't start next page call yet
        }
        mBaseWinesController.searchWine(mCurrentQuery, mAdapter.getCount(), LIMIT);
        mLoadingNextPage = true;
    }
}
