package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.followfriends.widget.InfluencerAccountsAdapter;
import com.delectable.mobile.ui.followfriends.widget.TwitterAccountsAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class FollowTwitterFriendsTabFragment extends BaseFollowFriendsTabFragment{

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ListView listview = (ListView) inflater
                .inflate(R.layout.fragment_listview_no_divider, container, false);
        mAdapter.setTopHeaderTitleResId(R.string.follow_friends_twitter);
        listview.setAdapter(mAdapter);
        return listview;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAccounts == null) {
            mAccountController.fetchTwitterSuggestions(getEventId());
        }
    }
}
