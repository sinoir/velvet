package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.FetchedAccountsFromContactsEvent;
import com.delectable.mobile.ui.followfriends.widget.BaseAccountsMinimalAdapter;
import com.delectable.mobile.ui.followfriends.widget.ContactsAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import javax.inject.Inject;

public class FollowContactsTabFragment extends BaseFollowFriendsTabFragment {

    private static final String TAG = FollowContactsTabFragment.class.getSimpleName();

    @Inject
    AccountController mAccountController;

    private ContactsAdapter mAdapter = new ContactsAdapter(this);

    @Override
    protected BaseAccountsMinimalAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected String getEventId() {
        return TAG;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ListView listview = (ListView) inflater
                .inflate(R.layout.fragment_listview, container, false);
        listview.setAdapter(mAdapter);
        return listview;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountController.fetchAccountsFromContacts();
    }

    public void onEventMainThread(FetchedAccountsFromContactsEvent event) {
        if (event.isSuccessful()) {
            mAccounts = event.getAccounts();
            mAdapter.setAccounts(event.getAccounts());
            mAdapter.notifyDataSetChanged();
            return;
        }
        //event error
        showToastError(event.getErrorMessage());
    }
}
