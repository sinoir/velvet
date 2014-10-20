package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.cache.FollowersFollowingModel;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.events.accounts.FollowAccountEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.profile.widget.FollowersAdapter;
import com.delectable.mobile.ui.profile.widget.FollowersRow;
import com.delectable.mobile.util.SafeAsyncTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * The base class for Following and Follower fragments. Leaves the implementation of the fetch up to
 * the subclass, as well as the event implementation.
 */
public abstract class BaseFollowersFragment extends BaseFragment
        implements AdapterView.OnItemClickListener, InfiniteScrollAdapter.ActionsHandler,
        FollowersRow.ActionsHandler {

    private static final String ACCOUNT_ID = "ACCOUNT_ID";

    private final String TAG = this.getClass().getSimpleName();

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.empty_state_layout)
    protected View mEmptyStateLayout;

    /**
     * In the layout, this covers the loading circle complete when it's set to visible, so there's
     * no need to hide the loading circle.
     */
    @InjectView(R.id.nothing_to_display_textview)
    protected FontTextView mNoFollowersText;

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected FollowersFollowingModel mListingModel;

    private FollowersAdapter mAdapter = new FollowersAdapter(this, this);

    private String mAccountId;

    private BaseListingResponse<AccountMinimal> mFollowerListing;

    /**
     * Flag to know when we are already fetching
     */
    private boolean mFetching;

    protected abstract BaseListingResponse<AccountMinimal> getCachedListing(String accountId);

    protected abstract void fetchAccounts(String accountId,
            BaseListingResponse<AccountMinimal> accountListing, boolean isPullToRefresh);

    protected void setArguments(String accountId) {
        Bundle args = new Bundle();
        args.putString(ACCOUNT_ID, accountId);
        this.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
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
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_listview_w_loading, container, false);
        ButterKnife.inject(this, view);

        mListView.setEmptyView(mEmptyStateLayout);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        if (mAdapter.getItems().isEmpty()) {
            Log.d(TAG, "onResume:loadLocalData");
            loadLocalData();
        }
    }

    private void loadLocalData() {
        new SafeAsyncTask<BaseListingResponse<AccountMinimal>>(this) {
            @Override
            protected BaseListingResponse<AccountMinimal> safeDoInBackground(Void[] params) {
                return getCachedListing(mAccountId);
            }

            @Override
            protected void safeOnPostExecute(BaseListingResponse<AccountMinimal> listing) {

                if (listing != null) {
                    mFollowerListing = listing;
                    //items were successfully retrieved from cache, set to view!
                    mAdapter.setItems(listing.getUpdates());
                    mAdapter.notifyDataSetChanged();
                }

                mFetching = true;
                if (mAdapter.getItems().isEmpty()) {
                    //only if there were no cache items do we make the call to fetch entries
                    //start first fetch for followers
                    fetchAccounts(mAccountId, null, false);
                } else {
                    //simulate a pull to refresh if there are items
                    fetchAccounts(mAccountId, mFollowerListing, true);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected void handleFetchFollowersEvent(UpdatedListingEvent<AccountMinimal> event) {
        if (!mAccountId.equals(event.getAccountId())) {
            return;
        }

        mFetching = false;

        if (mAdapter.getItems().isEmpty() || !event.isSuccessful()) {
            // TODO: No network Error state?
            mNoFollowersText.setVisibility(View.VISIBLE);
        }

        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
        } else if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }

        if (event.getListing() != null) {
            mFollowerListing = event.getListing();
            mAdapter.setItems(mFollowerListing.getUpdates());
            mAdapter.notifyDataSetChanged();
        }
        //if cacheListing is null, means there are no updates or an error happened
        //we don't let mFollowerListing get assigned null
    }

    public void onEventMainThread(FollowAccountEvent event) {
        if (!mAccountId.equals(event.getAccountId())) {
            return;
        }
        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }
    }


    @Override
    public void toggleFollow(AccountMinimal account, boolean isFollowing) {
        int relationship = isFollowing ? AccountMinimal.RELATION_TYPE_FOLLOWING
                : AccountMinimal.RELATION_TYPE_NONE;
        account.setCurrentUserRelationship(relationship);
        mAccountController.followAccount(account.getId(), isFollowing);
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

        if (mFetching) {
            return;
        }

        if (mFollowerListing == null) {
            //reached end of list/there are no items, we do nothing.
            //though, this should never be null bc the fragment doesn't it allow it to be.
            return;
        }

        if (mFollowerListing.getMore()) {
            mFetching = true;
            mNoFollowersText.setVisibility(View.GONE);
            fetchAccounts(mAccountId, mFollowerListing, false);
        }
    }
}
