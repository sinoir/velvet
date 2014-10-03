package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.FetchFriendSuggestionsEvent;
import com.delectable.mobile.events.accounts.FollowAccountEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.followfriends.widget.BaseAccountsMinimalAdapter;
import com.delectable.mobile.ui.followfriends.widget.FollowActionsHandler;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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

    private static final String TAG = BaseFollowFriendsTabFragment.class.getSimpleName();

    protected ArrayList<AccountMinimal> mAccounts;

    @Inject
    protected AccountController mAccountController;

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

        // Ignore FB Auth Errors when User hasn't authenticated
        if (ErrorUtil.FACEBOOK_AUTH_NOT_FOUND == event.getErrorCode()) {
            // TODO: Show FB Connect Empty state with "Connect to FB"
            return;
        }
        //event error
        showToastError(event.getErrorMessage());
    }

    public void onEventMainThread(FollowAccountEvent event) {
        String accountId = event.getAccountId();
        AccountMinimal account = mAccountsExpectingUpdate.remove(accountId);
        Integer relationship = mAccountExpectedRelationship.remove(accountId);
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

    @Override
    public void inviteContact(TaggeeContact contact) {
        // TODO: Invite Contact Dialog someday?  No Designs.. many ways of doing this
        // Right now: Pick first email from contacts, and try to open email app
        // If no email exists, try sending SMS
        if (contact.getEmailAddresses().size() > 0) {
            inviteViaEmail(contact.getEmailAddresses().get(0));
        } else if (contact.getPhoneNumbers().size() > 0) {
            inviteViaSms(contact.getPhoneNumbers().get(0));
        }
    }

    @Override
    public void showUserProfile(String accountId) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, accountId);
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    private void inviteViaEmail(String email) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + email));
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_body));
            startActivity(Intent.createChooser(emailIntent, "Send mail:"));
        } catch (ActivityNotFoundException ex) {
            showToastError(getString(R.string.error_no_email_client));
        }
    }

    private void inviteViaSms(String phone) {
        try {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phone);
            smsIntent.putExtra("sms_body", getString(R.string.invite_body));
            startActivity(smsIntent);
        } catch (ActivityNotFoundException ex) {
            Log.e(TAG, "SMS Failed: ", ex);
            showToastError(getString(R.string.error_sms_failed));
        }
    }
}
