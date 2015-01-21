package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.events.accounts.AssociateFacebookEvent;
import com.delectable.mobile.ui.followfriends.widget.ContactsAdapter;
import com.delectable.mobile.util.DateHelperUtil;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FollowFacebookFriendsTabFragment extends BaseFollowFriendsTabFragment {

    private static final String TAG = FollowFacebookFriendsTabFragment.class.getSimpleName();

    public static final String FACEBOOK_CONNECT = TAG + "_FACEBOOK_CONNECT";

    private LoginButton mHiddenFacebookLoginButton;

    private UiLifecycleHelper mFacebookUiHelper;

    private ContactsAdapter mAdapter = new ContactsAdapter(this);

    @Override
    public ContactsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected String getEventId() {
        return TAG;
    }

    @Override
    protected void fetchAccounts() {
        if (UserInfo.getAccountPrivate(getActivity()).isFacebookConnected()) {
            mAccountController.fetchFacebookSuggestions(getEventId());
            mEmptyTextButtonView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyTextButtonView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFacebookUiHelper = new UiLifecycleHelper(getActivity(), mFacebookCallback);
        mFacebookUiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter.setTopHeaderTitleResId(R.string.follow_friends_facebook);
        mEmptyTextView.setText(R.string.empty_facebook);

        mConnectButton.setText(R.string.connect_facebook_button);
        mConnectButton.setTextColor(getResources().getColor(R.color.com_facebook_blue));
        mConnectButton.setIconDrawable(getResources().getDrawable(R.drawable.ic_fb));

        mHiddenFacebookLoginButton = new LoginButton(getActivity());
        mHiddenFacebookLoginButton.setFragment(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFacebookUiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFacebookUiHelper.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFacebookUiHelper.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFacebookUiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFacebookUiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookUiHelper.onActivityResult(requestCode, resultCode, data);
        mHiddenFacebookLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onConnectClick() {
        mHiddenFacebookLoginButton.performClick();
    }

    private Session.StatusCallback mFacebookCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG + ".Facebook", "Session State: " + session.getState());
            Log.d(TAG + ".Facebook", "Session:" + session);
            Log.d(TAG + ".Facebook", "Exception:" + exception);

            if (session.getState().equals(SessionState.OPENING)) {
                return;
            }

            if (state.isOpened()) {
                facebookConnect();
                return;
            }

            //logout event
            if (state.isClosed()) {
                return;
            }

            // TODO: Handle more errors and other conditions.
            showToastError(getString(R.string.error_facebook_connect_failed));
        }
    };

    public void facebookConnect() {
        mEmptyTextButtonView.setVisibility(View.INVISIBLE); //show spinner
        Session session = Session.getActiveSession();
        mAccountController.associateFacebook(FACEBOOK_CONNECT, session.getAccessToken(),
                DateHelperUtil.doubleFromDate(session.getExpirationDate()));
    }

    public void onEventMainThread(AssociateFacebookEvent event) {
        mEmptyTextButtonView.setVisibility(View.VISIBLE);
        if (event.isSuccessful()) {
            fetchAccounts();
        } else {
            showToastError(event.getErrorMessage());
            // Close FB Session
            Session session = Session.getActiveSession();
            if (session != null) {
                session.closeAndClearTokenInformation();
            }
        }
    }
}
