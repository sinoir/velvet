package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.CapturesPendingCapturesListingModel;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContext;
import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.common.widget.OverScrollByListView;
import com.delectable.mobile.ui.profile.widget.CapturesPendingCapturesAdapter;
import com.delectable.mobile.ui.profile.widget.MinimalPendingCaptureRow;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;
import com.delectable.mobile.util.SafeAsyncTask;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

// TODO / Note: Abstract something from FollowFeedTabFragment, these are almost identical.
public class RecentCapturesTabFragment extends BaseCaptureDetailsFragment implements
        OverScrollByListView.ScrollByCallback, InfiniteScrollAdapter.ActionsHandler,
        MinimalPendingCaptureRow.ActionsHandler {

    private static final String TAG = RecentCapturesTabFragment.class.getSimpleName();

    private static final String CAPTURES_REQ = TAG + "_captures_req";

    private static final String ACCOUNT_ID = "ACCOUNT_ID";

    @Inject
    CapturesPendingCapturesListingModel mListingModel;

    @Inject
    AccountController mAccountController;

    @InjectView(android.R.id.list)
    protected OverScrollByListView mListView;

    @InjectView(R.id.empty_state_layout)
    protected View mEmptyStateLayout;

    /**
     * In the layout, this covers the loading circle complete when it's set to visible, so there's
     * no need to hide the loading circle.
     */
    @InjectView(R.id.nothing_to_display_textview)
    protected FontTextView mNoCapturesTextView;


    private CapturesPendingCapturesAdapter mAdapter;

    private Listing<BaseListingElement> mCapturesListing;

    private String mAccountId;

    private Callback mCallback;

    private boolean mFetching;

    private String mEmptyStateText;

    public RecentCapturesTabFragment() {
        // Required empty public constructor
    }

    public static RecentCapturesTabFragment newInstance(String userId) {
        RecentCapturesTabFragment fragment = new RecentCapturesTabFragment();
        Bundle args = new Bundle();
        args.putString(ACCOUNT_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        Bundle args = getArguments();

        if (args == null) {
            throw new RuntimeException(TAG + " needs to be initialized with an accountId");
        }

        mAccountId = args.getString(ACCOUNT_ID);
        mAdapter = new CapturesPendingCapturesAdapter(this, this, this, mAccountId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_layout, container, false);
        ButterKnife.inject(this, view);

        mNoCapturesTextView.setText(mEmptyStateText);

        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyStateLayout);
        mListView.setCallback(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //start first fetch if there are no items
        if (mAdapter.getItems().isEmpty()) {
            loadLocalData();
        }
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private void loadLocalData() {
        new SafeAsyncTask<Listing<BaseListingElement>>(this) {
            @Override
            protected Listing<BaseListingElement> safeDoInBackground(Void[] params) {
                return mListingModel.getUserCaptures(mAccountId);
            }

            @Override
            protected void safeOnPostExecute(Listing<BaseListingElement> listing) {

                if (listing != null) {
                    mCapturesListing = listing;
                    //items were successfully retrieved from cache, set to view!
                    mAdapter.setItems(listing.getUpdates());
                    mAdapter.notifyDataSetChanged();
                }

                mFetching = true;
                if (mAdapter.getItems().isEmpty()) {
                    //only if there were no cache items do we make the call to fetch entries
                    mAccountController.fetchAccountCaptures(CAPTURES_REQ, CapturesContext.DETAILS,
                            mAccountId, null, false);
                } else {
                    //simulate a pull to refresh if there are items
                    mAccountController.fetchAccountCaptures(CAPTURES_REQ, CapturesContext.DETAILS,
                            mAccountId, mCapturesListing, true);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onEventMainThread(UpdatedListingEvent<BaseListingElement> event) {
        if (!CAPTURES_REQ.equals(event.getRequestId())) {
            return;
        }
        if (!mAccountId.equals(event.getAccountId())) {
            return;
        }

        mFetching = false;

        if (mAdapter.getItems().isEmpty()) {
            mNoCapturesTextView.setVisibility(View.VISIBLE);
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }

        if (event.getListing() != null) {
            mCapturesListing = event.getListing();
            mAdapter.setItems(mCapturesListing.getUpdates());
            mAdapter.notifyDataSetChanged();
        }
        //if cacheListing is null, means there are no updates
        //we don't let mFollowerListing get assigned null
    }

    @Override
    public void shouldLoadNextPage() {
        if (mFetching) {
            return;
        }

        if (mCapturesListing == null) {
            //reached end of list/there are no items, we do nothing.
            //though, this should never be null bc the fragment doesn't it allow it to be.
            return;
        }

        if (mCapturesListing.getMore()) {
            mFetching = true;
            //mNoFollowersText.setVisibility(View.GONE);
            mAccountController.fetchAccountCaptures(CAPTURES_REQ, CapturesContext.DETAILS,
                    mAccountId, mCapturesListing, false);
        }
    }

    @Override
    public void launchWineProfile(CaptureDetails captureDetails) {
        Intent intent = new Intent();
        // Launch WineProfile if the capture matched a wine, otherwise launch the capture details
        if (captureDetails.getWineProfile() != null) {
            intent = WineProfileActivity.newIntent(getActivity(), captureDetails.getWineProfile(),
                    captureDetails.getPhoto());
        } else {
            intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                    captureDetails.getId());
            intent.setClass(getActivity(), CaptureDetailsActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void reloadLocalData() {
        loadLocalData();
    }

    @Override
    public void dataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void overScrolledY(int deltaY, int scrollY, boolean isTouchEvent) {
        // Check if it overscrolled on the top
        if (scrollY == 0 && deltaY < 0 && mCallback != null) {
            Log.d(TAG, "ListOverScroll DeltaY: " + deltaY + " ScrollY: " + scrollY);
            mCallback.onCaptureListOverScrolledTop();
            // Cancel any further scrolling, to prevent strange behavior
            mListView.cancelScrolling();
        }
    }

    @Override
    public void deleteCapture(CaptureDetails capture) {
        super.deleteCapture(capture);
        mAdapter.getItems().remove(capture);
        mAdapter.notifyDataSetChanged();
    }

    public void setEmptyStateText(String emptyText) {
        mEmptyStateText = emptyText;
        if (mNoCapturesTextView != null) {
            mNoCapturesTextView.setText(emptyText);
        }
    }

    //region PendingCapture callbacks from MinimalPendingCaptureRow
    @Override
    public void launchWineProfile(PendingCapture capture) {

    }

    @Override
    public void addRatingAndComment(PendingCapture capture) {

    }

    @Override
    public void discardCapture(PendingCapture capture) {

    }
    //endregion

    public interface Callback {

        /**
         * Helper callback to pass up the ListView OverScroll when the user overscrolled the top of
         * the ListView
         */
        public void onCaptureListOverScrolledTop();
    }
}
