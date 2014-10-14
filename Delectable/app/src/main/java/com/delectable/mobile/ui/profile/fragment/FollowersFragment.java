package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.events.UpdatedListingEvent;

import android.os.Bundle;
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
    protected BaseListingResponse<AccountMinimal> getCachedListing(String accountId) {
        return mListingModel.getFollowersListing(accountId);
    }

    @Override
    protected void fetchAccounts(String accountId,
            BaseListingResponse<AccountMinimal> accountListing) {
        mAccountController.fetchFollowers(FOLLOWERS_REQ, accountId, accountListing, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setActionBarTitle(getString(R.string.followers_title));
        mNoFollowersText.setText(R.string.followers_no_followers);
        return view;
    }

    public void onEventMainThread(UpdatedListingEvent<AccountMinimal> event) {
        if (!FOLLOWERS_REQ.equals(event.getRequestId())) {
            return;
        }
        handleFetchFollowersEvent(event);
    }
}
