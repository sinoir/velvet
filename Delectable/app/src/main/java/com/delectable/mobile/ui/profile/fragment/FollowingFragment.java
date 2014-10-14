package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.events.UpdatedListingEvent;
import com.delectable.mobile.events.accounts.UpdatedFollowingsEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FollowingFragment extends BaseFollowersFragment {

    private static final String TAG = FollowingFragment.class.getSimpleName();

    private static final String FOLLOWING_REQ = TAG + "_following_req";


    public static FollowingFragment newInstance(String accountId) {
        FollowingFragment fragment = new FollowingFragment();
        fragment.setArguments(accountId);
        return fragment;
    }

    @Override
    protected BaseListingResponse<AccountMinimal> getCachedListing(String accountId) {
        return mListingModel.getFollowingListing(accountId);
    }

    @Override
    protected void fetchAccounts(String accountId,
            BaseListingResponse<AccountMinimal> accountListing) {
        mAccountController.fetchFollowings(FOLLOWING_REQ, accountId, accountListing, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setActionBarTitle(getString(R.string.following_title));
        mNoFollowersText.setText(R.string.following_not_following_anyone);
        return view;
    }

    public void onEventMainThread(UpdatedListingEvent<AccountMinimal> event) {
        if (!FOLLOWING_REQ.equals(event.getRequestId())) {
            return;
        }
        handleFetchFollowersEvent(event);
    }
}
