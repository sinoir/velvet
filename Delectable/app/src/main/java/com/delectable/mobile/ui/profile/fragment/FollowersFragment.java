package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.UpdatedFollowersEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.profile.widget.FollowersAdapter;
import com.delectable.mobile.ui.profile.widget.FollowersRow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import javax.inject.Inject;

public class FollowersFragment extends BaseFragment
        implements AdapterView.OnItemClickListener, InfiniteScrollAdapter.ActionsHandler,
        FollowersRow.ActionsHandler {

    private static final String TAG = FollowersFragment.class.getSimpleName();

    private static final String ACCOUNT_ID = "ACCOUNT_ID";

    @Inject
    protected AccountController mAccountController;

    private FollowersAdapter mAdapter = new FollowersAdapter(this, this);

    private String mAccountId;

    private ArrayList<AccountMinimal> mAccounts;

    private BaseListingResponse<AccountMinimal> mFollowerListing;

    /**
     * Flag to know when we are already fetching
     */
    private boolean mFetching;


    public static FollowersFragment newInstance(String accountId) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putString(ACCOUNT_ID, accountId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        Bundle args = getArguments();
        if (args == null) {
            throw new RuntimeException(TAG + " needs to be instantiated with an accountId");
        }

        mAccountId = args.getString(ACCOUNT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview_w_loading, container, false);
        ListView listview = (ListView) view.findViewById(R.id.list_view);
        View emptyview = view.findViewById(R.id.progress_bar);
        listview.setEmptyView(emptyview);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //start first fetch for followers
        if (mAccounts == null) {
            mFetching = true;
            mAccountController.fetchFollowers(mAccountId, null);
        }
    }

    public void onEventMainThread(UpdatedFollowersEvent event) {
        Log.d(TAG, "UpdatedFollowersEvent: " + event.getAccountId());
        Log.d(TAG, "mAccountId: " + mAccountId);

        if (!mAccountId.equals(event.getAccountId())) {
            return;
        }
        Log.d(TAG, "UpdatedFollowersEvent: match");

        mFetching = false;
        if (!event.isSuccessful()) {
            Log.d(TAG, "UpdatedFollowersEvent: fail");
            showToastError(event.getErrorMessage());
            return;
        }

        Log.d(TAG, "UpdatedFollowersEvent: success");


        //lazily instantiate
        if (mAccounts == null) {
            mAccounts = new ArrayList<AccountMinimal>();
            mAdapter.setItems(mAccounts);
        }

        mFollowerListing = event.getFollowerListing();
        if (mFollowerListing != null) {
            mFollowerListing.combineInto(mAccounts);
            //mAdapter.setItems(mAccounts);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void toggleFollow(AccountMinimal account, boolean isFollowing) {
        int relationship = isFollowing ? AccountMinimal.RELATION_TYPE_FOLLOWING
                : AccountMinimal.RELATION_TYPE_NONE;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AccountMinimal account = mAdapter.getItem(position);
        launchUserProfile(account);
    }

    private void launchUserProfile(AccountMinimal account) {
        Intent intent = UserProfileActivity.newIntent(getActivity(), account.getId());
        startActivity(intent);
    }

    @Override
    public void shouldLoadNextPage() {
        Log.d(TAG, "shouldLoadNextPage");
        if (mFetching) {
            return;
        }
        if (mFollowerListing.getMore()) {
            mAccountController.fetchFollowers(mAccountId, mFollowerListing);
            mFetching = true;
        }
    }


}
