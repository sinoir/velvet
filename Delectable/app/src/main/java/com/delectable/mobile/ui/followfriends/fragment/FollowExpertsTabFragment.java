package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.FetchFriendSuggestionsEvent;
import com.delectable.mobile.events.accounts.FollowAccountEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.followfriends.widget.FollowExpertsRow;
import com.delectable.mobile.ui.followfriends.widget.InfluencerAccountsAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class FollowExpertsTabFragment extends BaseFragment
        implements FollowExpertsRow.ActionsHandler {

    @Inject
    AccountController mAccountController;

    private static final String TAG = FollowExpertsTabFragment.class.getSimpleName();

    private InfluencerAccountsAdapter mAdapter = new InfluencerAccountsAdapter(this);

    private ArrayList<AccountMinimal> mAccounts;

    /**
     * these maps are used to retain references to Account objects expecting updates to their
     * relationship status. This way, when the FollowAccountEvent returns, we don't have to iterate
     * through our account list to find the account object to modify.
     */
    private HashMap<String, AccountMinimal> mAccountsExpectingUpdate
            = new HashMap<String, AccountMinimal>();

    private HashMap<String, Integer> mAccountExpectedRelationship
            = new HashMap<String, Integer>();

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

    public void onEventMainThread(FollowAccountEvent event) {

        String accountId = event.getAccountId();
        AccountMinimal account = mAccountsExpectingUpdate.remove(accountId);
        int relationship = mAccountExpectedRelationship.remove(accountId);

        if (event.isSuccessful()) {
            account.setCurrentUserRelationship(relationship);
        } else {
            showToastError(event.getErrorMessage());
        }
        //will reset following toggle button back to original setting if error
        mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(FetchFriendSuggestionsEvent event) {
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
        int relationship = isFollowing ? AccountMinimal.RELATION_TYPE_FOLLOWING
                : AccountMinimal.RELATION_TYPE_NONE;
        mAccountsExpectingUpdate.put(account.getId(), account);
        mAccountExpectedRelationship.put(account.getId(), relationship);
        mAccountController.followAccount(account.getId(), isFollowing);
    }
}
