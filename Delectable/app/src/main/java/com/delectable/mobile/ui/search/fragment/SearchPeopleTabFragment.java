package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.FollowAccountEvent;
import com.delectable.mobile.api.events.accounts.SearchAccountsEvent;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.search.widget.AccountSearchAdapter;
import com.delectable.mobile.ui.search.widget.SearchPeopleRow;
import com.delectable.mobile.util.AnalyticsUtil;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class SearchPeopleTabFragment extends BaseSearchTabFragment
        implements SearchPeopleRow.ActionsHandler, InfiniteScrollAdapter.ActionsHandler {

    private static final String TAG = SearchPeopleTabFragment.class.getSimpleName();

    //number of items we fetch at a time
    private final int LIMIT = 20; //TODO 20 items per fetch/more?

    @Inject
    protected AccountController mAccountController;

    private AccountSearchAdapter mAdapter = new AccountSearchAdapter(this, this);

    private String mCurrentQuery;

    private boolean mLoadingNextPage = false;

    /**
     * If zero is returned for the current search query, then we know we've reached the end of the
     * list. Should disable infinite scroll. Gets reset with each new search.
     */
    private boolean mEndOfList = false;

    /**
     * these maps are used to retain references to Account objects expecting updates to their
     * relationship status. This way, when the FollowAccountEvent returns, we don't have to iterate
     * through our account list to find the account object to modify.
     */
    private HashMap<String, AccountSearch> mAccountsExpectingUpdate
            = new HashMap<String, AccountSearch>();

    private HashMap<String, Integer> mAccountExpectedRelationship
            = new HashMap<String, Integer>();

    @Override
    protected BaseAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mSearchView.setQueryHint(getString(R.string.search_people_hint));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        super.onQueryTextSubmit(query);
        mEndOfList = false; //new search query, reset this flag
        mAdapter.getItems().clear(); //clear current data set
        mCurrentQuery = query;
        mAccountController.searchAccounts(query, 0, LIMIT);
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.GONE);

        mAnalytics.trackSearch(AnalyticsUtil.SEARCH_PEOPLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onEventMainThread(SearchAccountsEvent event) {
        mProgressBar.setVisibility(View.GONE);
        mLoadingNextPage = false;
        if (event.isSuccessful()) {

            ArrayList<SearchHit<AccountSearch>> hits = event.getResult().getHits();
            if (hits.size() == 0) {
                mEndOfList = true;
            }

            mAdapter.getItems().addAll(hits);
            mAdapter.notifyDataSetChanged();
            mEmptyStateTextView.setText(getResources().getString(R.string.empty_search_people));
        } else if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
        } else {
            showToastError(event.getErrorMessage());
            mEmptyStateTextView.setText(event.getErrorMessage()); //TODO no empty state designs yet
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AccountSearch account = mAdapter.getItem(position);
        launchUserProfile(account);
    }

    private void launchUserProfile(AccountSearch account) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, account.getId());
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void toggleFollow(AccountSearch account, boolean isFollowing) {
        int relationship = isFollowing ? AccountSearch.RELATION_TYPE_FOLLOWING
                : AccountSearch.RELATION_TYPE_NONE;
        mAccountsExpectingUpdate.put(account.getId(), account);
        mAccountExpectedRelationship.put(account.getId(), relationship);
        mAccountController.followAccount(account.getId(), isFollowing);
    }

    public void onEventMainThread(FollowAccountEvent event) {
        String accountId = event.getAccountId();
        AccountSearch account = mAccountsExpectingUpdate.remove(accountId);
        int relationship = mAccountExpectedRelationship.remove(accountId);
        if (account == null) {
            return; //account didn't exist in the hashmap, means this event wasn't called from this fragment
        }
        if (event.isSuccessful()) {
            account.setCurrentUserRelationship(relationship);
        } else if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
        } else {
            showToastError(event.getErrorMessage());
        }
        //will reset following toggle button back to original setting if error
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void shouldLoadNextPage() {
        if (mEndOfList) {
            return; //end of list, don't infinite scroll
        }
        if (mLoadingNextPage) {
            return; //current loading next page, don't start next page call yet
        }
        mAccountController.searchAccounts(mCurrentQuery, mAdapter.getCount(), LIMIT);
        mLoadingNextPage = true;
    }

}
