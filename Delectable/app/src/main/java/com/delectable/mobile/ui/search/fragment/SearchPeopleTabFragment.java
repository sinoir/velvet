package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.SearchAccountsEvent;
import com.delectable.mobile.ui.search.widget.AccountSearchAdapter;
import com.delectable.mobile.ui.search.widget.SearchPeopleRow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import javax.inject.Inject;

public class SearchPeopleTabFragment extends BaseSearchTabFragment
        implements AdapterView.OnItemClickListener, SearchPeopleRow.ActionsHandler {

    private static final String TAG = SearchPeopleTabFragment.class.getSimpleName();

    private AccountSearchAdapter mAdapter = new AccountSearchAdapter(this);

    @Inject
    AccountController mAccountController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ListView listview = (ListView) inflater.inflate(R.layout.fragment_listview, container,
                false);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(this);

        //TODO make better one, no designs for this empty state
        TextView tv = new TextView(getActivity());
        tv.setText(TAG);
        listview.setEmptyView(tv);
        return listview;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAccountController.searchAccounts(query, 0, 20);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onEventMainThread(SearchAccountsEvent event) {
        if (event.isSuccessful()) {
            mAdapter.setHits(event.getResult().getHits());
            mAdapter.notifyDataSetChanged();
        } else {
            showToastError(event.getErrorMessage());
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
