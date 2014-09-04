package com.delectable.mobile.ui.search.fragment;


import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.controllers.BaseWineController;
import com.delectable.mobile.events.accounts.SearchAccountsEvent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

public class SearchPeopleTabFragment extends BaseSearchTabFragment {

    private static final String TAG = SearchPeopleTabFragment.class.getSimpleName();

    @Inject
    AccountController mAccountController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        TextView tv = new TextView(getActivity());
        tv.setText(TAG);
        return tv;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG + ".onQueryTextSubmit", query);
        mAccountController.searchAccounts(query, 0, 20);
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG + ".onQueryTextChange", newText);
        return false;
    }

    public void onEventMainThread(SearchAccountsEvent event) {
        //TODO handle response
        Log.d(TAG + ".onEventMainThread", "SearchAccountsEvent");
        if (event.isSuccessful()) {
            Log.d(TAG + ".SearchAccountsEvent", event.getResult().getHits().toString());
        } else {
            showToastError(event.getErrorMessage());
        }
    }
}
