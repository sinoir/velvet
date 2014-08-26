package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.FetchFriendSuggestionsEvent;
import com.delectable.mobile.events.accounts.FollowAccountEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.followfriends.widget.ContactsAdapter;
import com.delectable.mobile.ui.followfriends.widget.FollowActionsHandler;
import com.delectable.mobile.ui.followfriends.widget.FollowContactRow;
import com.delectable.mobile.ui.followfriends.widget.FollowExpertsRow;
import com.delectable.mobile.ui.followfriends.widget.InfluencerAccountsAdapter;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class FollowFacebookFriendsTabFragment extends BaseFollowFriendsTabFragment {

    private static final String TAG = FollowFacebookFriendsTabFragment.class.getSimpleName();

    private ContactsAdapter mAdapter = new ContactsAdapter(this);

    @Override
    public ContactsAdapter getAdapter() {
        return mAdapter;
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
        if (mAccounts == null) {
            mAccountController.fetchFacebookSuggestions();
        }
    }

}
