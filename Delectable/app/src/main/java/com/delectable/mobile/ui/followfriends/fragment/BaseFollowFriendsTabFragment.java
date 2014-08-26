package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.FetchFriendSuggestionsEvent;
import com.delectable.mobile.events.accounts.FollowAccountEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.followfriends.widget.BaseAccountsMinimalAdapter;
import com.delectable.mobile.ui.followfriends.widget.FollowActionsHandler;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * All the follow friends fragments are essentially composed of the same data, and array of
 * AccountMinimal. This handles the incoming AccountMinimal data, and also the follow account logic.
 * It leaves the retrieval of the data up to it's subclasses.
 */
public abstract class BaseFollowFriendsTabFragment extends BaseFragment
        implements FollowActionsHandler {

    protected ArrayList<AccountMinimal> mAccounts;

    @Inject
    protected AccountController mAccountController;

    private static final String TAG = BaseFollowFriendsTabFragment.class.getSimpleName();

    /**
     * these maps are used to retain references to Account objects expecting updates to their
     * relationship status. This way, when the FollowAccountEvent returns, we don't have to iterate
     * through our account list to find the account object to modify.
     */
    private HashMap<String, AccountMinimal> mAccountsExpectingUpdate
            = new HashMap<String, AccountMinimal>();

    private HashMap<String, Integer> mAccountExpectedRelationship
            = new HashMap<String, Integer>();

    protected abstract BaseAccountsMinimalAdapter getAdapter();

    /**
     * Used to ensure that the request sent matches the event that we receive.
     * @return
     */
    protected abstract String getEventId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    public void onEventMainThread(FetchFriendSuggestionsEvent event) {
        if (!event.getId().equals(getEventId())) {
            return; //id's don't match, this event wasn't from our request
        }
        if (event.isSuccessful()) {
            mAccounts = event.getAccounts();
            getAdapter().setAccounts(event.getAccounts());
            getAdapter().notifyDataSetChanged();
            return;
        }
        //event error
        showToastError(event.getErrorMessage());
    }

    public void onEventMainThread(FollowAccountEvent event) {
        String accountId = event.getAccountId();
        AccountMinimal account = mAccountsExpectingUpdate.remove(accountId);
        int relationship = mAccountExpectedRelationship.remove(accountId);
        if (account == null) {
            return; //account didn't exist in the hashmap, means this event wasn't called from this fragment
        }
        if (event.isSuccessful()) {
            account.setCurrentUserRelationship(relationship);
        } else {
            showToastError(event.getErrorMessage());
        }
        //will reset following toggle button back to original setting if error
        getAdapter().notifyDataSetChanged();
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
