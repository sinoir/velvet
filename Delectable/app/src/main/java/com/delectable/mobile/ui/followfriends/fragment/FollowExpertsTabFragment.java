package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.FetchInfluencerSuggestionsEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.followfriends.widget.FollowExpertsRow;
import com.delectable.mobile.ui.followfriends.widget.InfluencerAccountsAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

public class FollowExpertsTabFragment extends BaseFragment
        implements FollowExpertsRow.ActionsHandler {

    @Inject
    AccountController mAccountController;

    private static final String TAG = FollowExpertsTabFragment.class.getSimpleName();

    private InfluencerAccountsAdapter mAdapter = new InfluencerAccountsAdapter(this);

    private ArrayList<AccountMinimal> mAccounts;

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
        if (mAccounts == null) {
            mAccountController.fetchInfluencerSuggestions();
        }
    }

    public void onEventMainThread(FetchInfluencerSuggestionsEvent event) {
        if (event.isSuccessful()) {
            mAccounts = event.getAccounts();
            mAdapter.setAccounts(event.getAccounts());
            mAdapter.notifyDataSetChanged();
            return;
        }
        //event error
        showToastError(event.getErrorMessage());

    }

    @Override
    public void toggleFollow(AccountMinimal account, boolean isFollowing) {
        Log.d(TAG, account.getFname() + " " + isFollowing);

    }
}
