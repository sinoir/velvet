package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.FollowersFollowingModel;
import com.delectable.mobile.events.UpdatedListingEvent;
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
 * the subclass, as well as the event implemenation.
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
            BaseListingResponse<AccountMinimal> accountListing);


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

        if (mAdapter.getItems().isEmpty()) {
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

                if (mAdapter.getItems().isEmpty()) {
                    //only if there were no cache items do we make the call to fetch entries
                    mFetching = true;
                    //start first fetch for followers
                    fetchAccounts(mAccountId, null);
                }


            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected void handleFetchFollowersEvent(UpdatedListingEvent<AccountMinimal> event) {
        if (!mAccountId.equals(event.getAccountId())) {
            return;
        }

        mFetching = false;

        if (mAdapter.getItems().isEmpty()) {
            mNoFollowersText.setVisibility(View.VISIBLE);
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }

        mFollowerListing = event.getListing();
        if (mFollowerListing != null) {
            mAdapter.setItems(mFollowerListing.getUpdates());
        }
        //if cacheListing is null, means there are no updates

        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void toggleFollow(AccountMinimal account, boolean isFollowing) {
        int relationship = isFollowing ? AccountMinimal.RELATION_TYPE_FOLLOWING
                : AccountMinimal.RELATION_TYPE_NONE;
        //TODO implement follow
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
            return; //reached end of list/there are no items, we do nothing.
        }

        if (mFollowerListing.getMore()) {
            fetchAccounts(mAccountId, mFollowerListing);
            mFetching = true;
            mNoFollowersText.setVisibility(View.GONE);
        }
    }


}
