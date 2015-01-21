package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.events.accounts.AssociateTwitterEvent;
import com.delectable.mobile.ui.followfriends.widget.TwitterAccountsAdapter;
import com.delectable.mobile.util.TwitterUtil;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FollowTwitterFriendsTabFragment extends BaseFollowFriendsTabFragment {

    private static final String TAG = FollowTwitterFriendsTabFragment.class.getSimpleName();

    public static final String ASSOCIATE_TWITTER = TAG + "_ASSOCIATE_TWITTER";

    private TwitterLoginButton mHiddenTwitterButton;

    private TwitterAccountsAdapter mAdapter = new TwitterAccountsAdapter(this);

    @Override
    public TwitterAccountsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected String getEventId() {
        return TAG;
    }

    @Override
    protected void fetchAccounts() {
        if (TwitterUtil.isLoggedIn()) {
            mAccountController.fetchTwitterSuggestions(getEventId());
            mEmptyTextButtonView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyTextButtonView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter.setTopHeaderTitleResId(R.string.follow_friends_twitter);

        mEmptyTextView.setText(R.string.empty_twitter);

        mConnectButton.setTextColor(getResources().getColor(R.color.d_blue_twitter_tab));
        mConnectButton.setText(R.string.connect_twitter_button);
        mConnectButton.setIconDrawable(getResources().getDrawable(R.drawable.ic_find_twitter_pressed));

        mHiddenTwitterButton = new TwitterLoginButton(getActivity());
        mHiddenTwitterButton.setCallback(TwitterCallback);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHiddenTwitterButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onConnectClick() {
        mHiddenTwitterButton.performClick();
    }

    private Callback<TwitterSession> TwitterCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {
            mEmptyTextButtonView.setVisibility(View.INVISIBLE); //hiding this will show loading circle

            TwitterUtil.TwitterInfo twitterInfo = TwitterUtil.getTwitterInfo(twitterSessionResult);
            mAccountController.associateTwitter(ASSOCIATE_TWITTER, twitterInfo.twitterId, twitterInfo.token, twitterInfo.tokenSecret, twitterInfo.screenName);
        }

        @Override
        public void failure(TwitterException e) {
            //TODO debug this exception and show error, but don't show error if user clicked back intentionally
            Log.d(TAG, "Twitter auth error", e);
            Log.d(TAG, "Twitter error message:" + e.getMessage());
            //showToastError("Twitter authentication failed");
        }
    };

    public void onEventMainThread(AssociateTwitterEvent event) {
        if (event.isSuccessful()) {
            fetchAccounts();
        } else {
            showToastError(event.getErrorMessage());
            TwitterUtil.clearSession();
            mEmptyTextButtonView.setVisibility(View.VISIBLE);
        }
    }
}
