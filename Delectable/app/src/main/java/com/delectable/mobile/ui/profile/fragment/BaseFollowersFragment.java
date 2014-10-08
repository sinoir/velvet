package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.events.accounts.BaseFetchedFollowersEvent;
import com.delectable.mobile.events.accounts.UpdatedFollowersEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * The base class for Following and Follower fragments. Leaves the implementation of the fetch up to the subclass,
 * as well as the event implemenation.
 */
public abstract class BaseFollowersFragment extends BaseFragment
        implements AdapterView.OnItemClickListener, InfiniteScrollAdapter.ActionsHandler,
        FollowersRow.ActionsHandler {

    private static final String TAG = BaseFollowersFragment.class.getSimpleName();

    private static final String ACCOUNT_ID = "ACCOUNT_ID";


    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.empty_state_layout)
    protected View mEmptyStateLayout;

    /**
     * In the layout, this covers the loading circle complete when it's set to visible, so there's
     * no need to hide the loading circle.
     */
    @InjectView(R.id.no_followers_textview)
    protected FontTextView mNoFollowersText;

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

    protected abstract void fetchAccounts(String accountId, BaseListingResponse<AccountMinimal> accountListing);


    protected void setArguments(String accountId) {
        Bundle args = new Bundle();
        args.putString(ACCOUNT_ID, accountId);
        this.setArguments(args);
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
        ButterKnife.inject(this, view);

        mListView.setEmptyView(mEmptyStateLayout);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //start first fetch for followers
        if (mAccounts == null) {
            mFetching = true;
            mNoFollowersText.setVisibility(View.GONE);
            fetchAccounts(mAccountId, null);
        }
    }

    protected void handleFetchFollowersEvent(BaseFetchedFollowersEvent event) {
        if (!mAccountId.equals(event.getAccountId())) {
            return;
        }

        mFetching = false;

        //lazily instantiate
        if (mAccounts == null) {
            mAccounts = new ArrayList<AccountMinimal>();
            mAdapter.setItems(mAccounts);
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());

            //show no followers message if this was the first fetch, so that we're not showing the loading circle anymore
            //it's ok when there's already data, because that will be showing on screen already
            if (mAccounts.size() == 0) {
                mNoFollowersText.setVisibility(View.VISIBLE);
            }
            return;
        }

        mFollowerListing = event.getListing();
        if (mFollowerListing != null) {
            mFollowerListing.combineInto(mAccounts);
            //mAdapter.setItems(mAccounts);
        }
        if (mAccounts.size() == 0) {
            mNoFollowersText.setVisibility(View.VISIBLE);
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
            fetchAccounts(mAccountId, mFollowerListing);
            mFetching = true;
            mNoFollowersText.setVisibility(View.GONE);
        }
    }


}
