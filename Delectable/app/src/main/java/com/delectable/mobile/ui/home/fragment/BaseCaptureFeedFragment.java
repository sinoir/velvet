package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.CaptureDetailsAdapter;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.common.widget.NestedSwipeRefreshLayout;
import com.delectable.mobile.util.SafeAsyncTask;
import com.melnykov.fab.FloatingActionButton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class BaseCaptureFeedFragment extends BaseCaptureDetailsFragment implements
        InfiniteScrollAdapter.ActionsHandler {

    private static final String ACCOUNT_ID = "ACCOUNT_ID";

    private final String TAG = this.getClass().getSimpleName();

    @InjectView(R.id.swipe_container)
    protected NestedSwipeRefreshLayout mRefreshContainer;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.camera_button)
    protected FloatingActionButton mCameraButton;

    private CaptureDetailsAdapter mAdapter;

    private Listing<CaptureDetails, String> mCapturesListing;

    private boolean mFetching;

    public BaseCaptureFeedFragment() {
        // Required empty public constructor
    }

    protected static Bundle bundleArgs(String accountId) {
        Bundle args = new Bundle();
        args.putString(ACCOUNT_ID, accountId);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        if (getArguments() == null) {
            throw new RuntimeException(TAG + " needs to be initialized with an accountId");
        }

        String accountId = getArguments().getString(ACCOUNT_ID);
        mAdapter = new CaptureDetailsAdapter(this, this, accountId);
        mAdapter.setRowType(CaptureDetailsAdapter.RowType.DETAIL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_home_follow_feed_tab, container, false);
        ButterKnife.inject(this, view);

        mRefreshContainer.setListView(mListView);
        mRefreshContainer
                .setColorScheme(R.color.d_soft_amber_25op, R.color.d_edward_25op,
                        R.color.d_soft_amber_25op, R.color.d_edward_25op);

        mListView.setAdapter(mAdapter);

        //pull to refresh setup
        mRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Setup Floating Camera Button
        mCameraButton.attachToListView(mListView);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWineCapture();
            }
        });

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
        mRefreshContainer.setRefreshing(true);
        mFetching = true;
        new SafeAsyncTask<Listing<CaptureDetails, String>>(this) {
            @Override
            protected Listing<CaptureDetails, String> safeDoInBackground(Void[] params) {
                Log.d(TAG, "loadLocalData:doInBg");
                return getCachedFeed();
            }

            @Override
            protected void safeOnPostExecute(Listing<CaptureDetails, String> listing) {
                Log.d(TAG, "loadLocalData:returnedFromCache");
                if (listing != null) {
                    Log.d(TAG, "loadLocalData:listingExists size: " + listing.getUpdates().size());
                    mCapturesListing = listing;
                    //items were successfully retrieved from cache, set to view!
                    mAdapter.setItems(listing.getUpdates());
                    mAdapter.notifyDataSetChanged();
                }

                if (mAdapter.getItems().isEmpty()) {
                    //only if there were no cache items do we make the call to fetch entries
                    fetchCaptures(null, false);
                } else {
                    Log.d(TAG, "loadLocalData:success");
                    //simulate a pull to refresh if there are items
                    fetchCaptures(mCapturesListing, true);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected abstract Listing<CaptureDetails, String> getCachedFeed();

    protected abstract void fetchCaptures(Listing<CaptureDetails, String> listing,
            boolean isPullToRefresh);

    private void refreshData() {
        Log.d(TAG, "refreshData");

        if (mFetching) {
            return; //already fetching, don't load
        }
        mFetching = true;
        mRefreshContainer.setRefreshing(true);
        fetchCaptures(mCapturesListing, true);
    }

    /**
     * The subclass will need to intercept the event and verify that the event was spawned from a
     * request the subclass.
     */
    public void onEventMainThread(UpdatedListingEvent<CaptureDetails> event) {
        Log.d(TAG, "UpdatedListingEvent:reqMatch:" + event.getRequestId());
        if (event.getListing() != null) {
            Log.d(TAG, "UpdatedListingEvent:etag:" + event.getListing().getETag());
            Log.d(TAG, "UpdatedListingEvent:size:" + event.getListing().getUpdates().size());
        }
        mFetching = false;

        if (mRefreshContainer.isRefreshing()) {
            mRefreshContainer.setRefreshing(false);
            Log.d(TAG, "UpdatedListingEvent:wasRefreshing");
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }
        Log.d(TAG, "UpdatedListingEvent:eventSuccess");

        if (event.getListing() != null) {
            mCapturesListing = event.getListing();
            mAdapter.setItems(mCapturesListing.getUpdates());
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "UpdatedListingEvent:capturesExist");
        }
        //if cacheListing is null, means there are no updates
        //we don't let mFollowerListing get assigned null

    }

    @Override
    public void shouldLoadNextPage() {
        Log.d(TAG, "shouldLoadNextPage");
        if (mFetching) {
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:notFetching");

        if (mCapturesListing == null) {
            //reached end of list/there are no items, we do nothing.
            //in theory, this should never be null because the getMore check below should stop it from loading
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:captureListingExists");

        if (mCapturesListing.getMore()) {
            mFetching = true;
            //mNoFollowersText.setVisibility(View.GONE);
            fetchCaptures(mCapturesListing, false);
            Log.d(TAG, "shouldLoadNextPage:moreTrue");
        }
    }

    @Override
    public void reloadLocalData() {
        loadLocalData();
    }

    @Override
    public void dataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
