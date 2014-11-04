package com.delectable.mobile.ui.followfriends.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.followfriends.widget.InfluencerAccountsAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FollowExpertsTabFragment extends BaseFollowFriendsTabFragment {

    private static final String TAG = FollowExpertsTabFragment.class.getSimpleName();

    private InfluencerAccountsAdapter mAdapter = new InfluencerAccountsAdapter(this);

    @Override
    public InfluencerAccountsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected String getEventId() {
        return TAG;
    }

    @Override
    protected void fetchAccounts() {
            mAccountController.fetchInfluencerSuggestions(getEventId());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mAdapter.setTopHeaderTitleResId(R.string.follow_friends_wine_experts);
        mConnectButton.setVisibility(View.GONE);
        return view;
    }

}
