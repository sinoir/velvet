package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.followfriends.widget.TwitterAccountsAdapter;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.util.TwitterUtil;
import com.twitter.sdk.android.Twitter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FollowTwitterFriendsTabFragment extends BaseFollowFriendsTabFragment {

    private static final String TAG = FollowTwitterFriendsTabFragment.class.getSimpleName();

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

        mConnectButton.setText(R.string.connect_twitter_button);
        mConnectButton.setIconDrawable(getResources().getDrawable(R.drawable.ic_find_twitter_pressed));
        return view;
    }
}
