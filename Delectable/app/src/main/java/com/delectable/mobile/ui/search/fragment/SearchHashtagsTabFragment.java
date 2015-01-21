package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.HashtagController;
import com.delectable.mobile.api.events.hashtags.SearchHashtagsEvent;
import com.delectable.mobile.api.models.CaptureFeed;
import com.delectable.mobile.api.models.HashtagResult;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.capture.activity.FeedActivity;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.search.widget.HashtagSearchAdapter;
import com.delectable.mobile.util.AnalyticsUtil;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

public class SearchHashtagsTabFragment extends BaseSearchTabFragment
        implements InfiniteScrollAdapter.ActionsHandler {

    private static final String TAG = SearchHashtagsTabFragment.class.getSimpleName();

    //number of items we fetch at a time
    private static final int LIMIT = 20;

    @Inject
    protected HashtagController mHashtagController;

    private HashtagSearchAdapter mAdapter = new HashtagSearchAdapter(this);

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
        mHashtagController.searchHashtagsWithCaptureCounts(query, 0, LIMIT);
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.GONE);

        mAnalytics.trackSearch(AnalyticsUtil.SEARCH_PEOPLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onEventMainThread(SearchHashtagsEvent event) {
        mProgressBar.setVisibility(View.GONE);
        mLoadingNextPage = false;
        if (!event.isSuccessful()) {
            if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
                showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
                return;
            }
            showToastError(event.getErrorMessage());
            mEmptyStateTextView.setText(event.getErrorMessage());

            return;
        }

        //successful request

        ArrayList<SearchHit<HashtagResult>> hits = event.getResult().getHits();
        if (hits.size() == 0) {
            mEndOfList = true;
        }

        mAdapter.getItems().addAll(hits);
        mAdapter.notifyDataSetChanged();
        mEmptyStateTextView.setText(getResources().getString(R.string.empty_search_hashtags));

        //will reset following toggle button back to original setting if error
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashtagResult account = mAdapter.getItem(position);
        String tag = account.getTag();

        //launch activity feed
        Intent intent = FeedActivity
                .newIntent(getActivity(), "hashtag:" + tag, CaptureFeed.CUSTOM, "#" + tag);
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
        mHashtagController
                .searchHashtagsWithCaptureCounts(mCurrentQuery, mAdapter.getCount(), LIMIT);
        mLoadingNextPage = true;
    }

}
