package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.SearchAccountsEvent;
import com.delectable.mobile.ui.search.widget.AccountSearchAdapter;
import com.delectable.mobile.ui.search.widget.SearchPeopleRow;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import javax.inject.Inject;

public class SearchPeopleTabFragment extends BaseSearchTabFragment
        implements SearchPeopleRow.ActionsHandler {

    private static final String TAG = SearchPeopleTabFragment.class.getSimpleName();

    private AccountSearchAdapter mAdapter = new AccountSearchAdapter(this);

    @Inject
    protected AccountController mAccountController;

    @Override
    protected BaseAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAccountController.searchAccounts(query, 0, 20);
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.GONE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onEventMainThread(SearchAccountsEvent event) {
        mProgressBar.setVisibility(View.GONE);
        if (event.isSuccessful()) {
            mAdapter.setHits(event.getResult().getHits());
            mAdapter.notifyDataSetChanged();
            mEmptyStateTextView.setText("No Results"); //TODO no empty state designs yet
        } else {
            showToastError(event.getErrorMessage());
            mEmptyStateTextView.setText(event.getErrorMessage()); //TODO no empty state designs yet
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AccountSearch account = mAdapter.getItem(position);
        goToUserProfile(account);
    }

    private void goToUserProfile(AccountSearch account) {
        //TODO implement
    }

    @Override
    public void toggleFollow(AccountSearch account, boolean isFollowing) {
        //TODO implement
    }
}
