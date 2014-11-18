package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.Listing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FollowersFragment extends BaseFollowersFragment {

    private static final String TAG = FollowersFragment.class.getSimpleName();

    private static final String FOLLOWERS_REQ = TAG + "_followers_req";

    public static FollowersFragment newInstance(String accountId) {
        FollowersFragment fragment = new FollowersFragment();
        fragment.setArguments(accountId);
        return fragment;
    }

    @Override
    protected Listing<AccountMinimal, String> getCachedListing(String accountId) {
        return mListingModel.getFollowersListing(accountId);
    }

    @Override
    protected void fetchAccounts(String accountId,
            Listing<AccountMinimal, String> accountListing, boolean isPullToRefresh) {
        mAccountController.fetchFollowers(FOLLOWERS_REQ, accountId, accountListing, isPullToRefresh);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setActionBarTitle(getString(R.string.followers_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mNoFollowersText.setText(R.string.followers_no_followers);
        return view;
    }

    public void onEventMainThread(UpdatedListingEvent<AccountMinimal, String> event) {
        if (!FOLLOWERS_REQ.equals(event.getRequestId())) {
            return;
        }
        handleFetchFollowersEvent(event);
    }
}
