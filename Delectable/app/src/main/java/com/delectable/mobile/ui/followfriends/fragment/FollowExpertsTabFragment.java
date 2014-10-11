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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater
                .inflate(R.layout.fragment_listview_no_divider, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        mAdapter.setTopHeaderTitleResId(R.string.follow_friends_wine_experts);
        listView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAccounts == null) {
            mAccountController.fetchInfluencerSuggestions(getEventId());
        }
    }

}
