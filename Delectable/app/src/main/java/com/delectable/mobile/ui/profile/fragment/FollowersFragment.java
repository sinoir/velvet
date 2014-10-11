package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.events.accounts.UpdatedFollowersEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FollowersFragment extends BaseFollowersFragment {

    private static final String TAG = FollowersFragment.class.getSimpleName();

    public static FollowersFragment newInstance(String accountId) {
        FollowersFragment fragment = new FollowersFragment();
        fragment.setArguments(accountId);
        return fragment;
    }

    @Override
    protected void fetchAccounts(String accountId,
            BaseListingResponse<AccountMinimal> accountListing) {
        mAccountController.fetchFollowers(accountId, accountListing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setActionBarTitle(getString(R.string.followers_title));
        mNoFollowersText.setText(R.string.followers_no_followers);
        return view;
    }

    public void onEventMainThread(UpdatedFollowersEvent event) {
        handleFetchFollowersEvent(event);
    }


}
