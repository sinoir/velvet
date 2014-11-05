package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.FetchFriendSuggestionsEvent;
import com.delectable.mobile.api.events.accounts.FollowAccountEvent;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.Delectabutton;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.followfriends.widget.BaseAccountsMinimalAdapter;
import com.delectable.mobile.ui.followfriends.widget.FollowActionsHandler;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * All the follow friends fragments are essentially composed of the same data, and array of
 * AccountMinimal. This handles the incoming AccountMinimal data, and also the follow account logic.
 * It leaves the retrieval of the data up to it's subclasses.
 */
public abstract class BaseFollowFriendsTabFragment extends BaseFragment
        implements FollowActionsHandler, AdapterView.OnItemClickListener {

    private static final String TAG = BaseFollowFriendsTabFragment.class.getSimpleName();

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.empty_state_layout)
    protected View mEmptyView;

    /**
     * A child of the empty view container. This View overlaps the progress circle. Set it to
     * visible in order to hide the progress circle when done fetching for data.
     */
    @InjectView(R.id.text_and_button_empty_view)
    protected View mEmptyTextButtonView;

    @InjectView(R.id.empty_text_view)
    protected FontTextView mEmptyTextView;

    @InjectView(R.id.connect_button)
    protected Delectabutton mConnectButton;

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

    protected abstract void fetchAccounts();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater
                .inflate(R.layout.fragment_listview_no_divider, container, false);
        ButterKnife.inject(this, view);
        mListView.setAdapter(getAdapter());
        mListView.setEmptyView(mEmptyView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getAdapter().getAccounts().isEmpty()) {
            mEmptyTextButtonView.setVisibility(View.INVISIBLE);
            fetchAccounts();
        }
    }

    @OnClick(R.id.connect_button)
    protected void onConnectClick() {
        // TODO connect to facebook/twitter
        mEventBus.post(new NavigationEvent(NavHeader.NAV_SETTINGS));
    }

    public void onEventMainThread(FetchFriendSuggestionsEvent event) {
        if (!event.getId().equals(getEventId())) {
            return; //id's don't match, this event wasn't from our request
        }

        mEmptyTextButtonView.setVisibility(View.VISIBLE);

        //ui shouldn't allow user to make a fetchFriends call if they don't have the credentials to do it
        //so this event will only be called if they already have twitter/fb connected
        mEmptyTextView.setText(R.string.follow_friends_no_friends_connected);
        mConnectButton.setVisibility(View.GONE);

        if (event.isSuccessful()) {

            getAdapter().setAccounts(event.getAccounts());
            getAdapter().notifyDataSetChanged();
            return;
        }

        // Ignore FB/Twitter Auth Errors when User hasn't authenticated
        if (ErrorUtil.FACEBOOK_AUTH_NOT_FOUND == event.getErrorCode() ||
                ErrorUtil.TWITTER_AUTH_NOT_FOUND == event.getErrorCode()) {
            // TODO: Show FB Connect Empty state with "Connect to FB"
            // TODO: Show Twitter Connect Empty state with "Connect to Twitter"
            return;
        }
        //event error
        mEmptyTextView.setText(R.string.follow_friends_error_retrieving_friends);
        Log.e(TAG, event.getErrorMessage());
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
        } else if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
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
            smsIntent.putExtra("address", phone);
            smsIntent.putExtra("sms_body", getString(R.string.invite_body));
            startActivity(Intent.createChooser(smsIntent, "Send SMS:"));
        } catch (ActivityNotFoundException ex) {
            Log.e(TAG, "SMS Failed: ", ex);
            showToastError(getString(R.string.error_sms_failed));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getAdapter().getItem(position) instanceof AccountMinimal) {
            launchUserProfile((AccountMinimal) getAdapter().getItem(position));
        }
    }

    private void launchUserProfile(AccountMinimal accountMinimal) {
        Intent intent = UserProfileActivity.newIntent(getActivity(), accountMinimal.getId());
        startActivity(intent);
    }

}
