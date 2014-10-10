package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.CaptureListingModel;
import com.delectable.mobile.events.accounts.UpdatedAccountCapturesEvent;
import com.delectable.mobile.model.api.accounts.AccountsCapturesRequest;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.CaptureDetailsAdapter;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.common.widget.OverScrollByListView;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;
import com.delectable.mobile.util.SafeAsyncTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

// TODO / Note: Abstract something from FollowFeedTabFragment, these are almost identical.
public class RecentCapturesTabFragment extends BaseCaptureDetailsFragment implements
        OverScrollByListView.ScrollByCallback, InfiniteScrollAdapter.ActionsHandler {

    private static final String TAG = RecentCapturesTabFragment.class.getSimpleName();

    private static final String ACCOUNT_ID = "ACCOUNT_ID";

    @Inject
    AccountController mAccountController;

    @Inject
    protected CaptureListingModel mCaptureListingModel;

    private View mView;

    private OverScrollByListView mListView;

    private CaptureDetailsAdapter mAdapter;

    private BaseListingResponse<CaptureDetails> mCapturesListing;

    private String mAccountId;

    private Callback mCallback;

    private boolean mFetching;

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
        mAdapter = new CaptureDetailsAdapter(this, this, mAccountId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.list_view_layout, container, false);
        mListView = (OverScrollByListView) mView.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setCallback(this);
        return mView;
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
        new SafeAsyncTask<BaseListingResponse<CaptureDetails>>(this) {
            @Override
            protected BaseListingResponse<CaptureDetails> safeDoInBackground(Void[] params) {
                return mCaptureListingModel.getListing(mAccountId);
            }

            @Override
            protected void safeOnPostExecute(BaseListingResponse<CaptureDetails> listing) {

                if (listing != null) {
                    mCapturesListing = listing;
                    //items were successfully retrieved from cache, set to view!
                    mAdapter.setItems(listing.getUpdates());
                    mAdapter.notifyDataSetChanged();
                    return;
                }

                //only if there were no cache items do we make the call to fetch entries
                mFetching = true;
                mAccountController.fetchAccountCaptures(AccountsCapturesRequest.Context.DETAILS,
                        mAccountId, null, false);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onEventMainThread(UpdatedAccountCapturesEvent event) {
        if (!mAccountId.equals(event.getAccountId())) {
            return;
        }

        mFetching = false;

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());

            //show no followers message if this was the first fetch, so that we're not showing the loading circle anymore
            //it's ok when there's already data, because that will be showing on screen already
            if (mAdapter.getItems().isEmpty()) {
                //TODO noCapturesText.setVisibility(View.VISIBLE);
            }
            return;
        }

        mCapturesListing = event.getListing();

        if (mCapturesListing != null) {
            mAdapter.setItems(mCapturesListing.getUpdates());
        }
        //if cacheListing is null, means there are no updates

        if (mAdapter.getItems().isEmpty()) {
            //TODO noCapturesText.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void shouldLoadNextPage() {
        Log.d(TAG, "shouldLoadNextPage");
        if (mFetching) {
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:notFetching");

        if (mCapturesListing == null) {
            return; //reached end of list/there are no items, we do nothing.
        }
        Log.d(TAG, "shouldLoadNextPage:CapturesListingExists");

        mFetching = true;
        mAccountController.fetchAccountCaptures(AccountsCapturesRequest.Context.DETAILS,
                mAccountId, mCapturesListing, false);
        //mNoFollowersText.setVisibility(View.GONE);
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


    public interface Callback {

        /**
         * Helper callback to pass up the ListView OverScroll when the user overscrolled the top of
         * the ListView
         */
        public void onCaptureListOverScrolledTop();
    }
}
