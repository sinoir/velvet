package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.controllers.CaptureController;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.events.captures.UpdatedUserCaptureFeedEvent;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.FollowFeedAdapter;
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

import java.util.ArrayList;

import javax.inject.Inject;

// TODO / Note: Abstract something from FollowFeedTabFragment, these are almost identical.
public class RecentCapturesTabFragment extends BaseCaptureDetailsFragment implements
        OverScrollByListView.ScrollByCallback {

    private static final String TAG = RecentCapturesTabFragment.class.getSimpleName();

    private static final String sArgsUserId = "sArgsUserId";

    @Inject
    CaptureController mCaptureController;

    @Inject
    CaptureDetailsListingModel mCaptureDetailsListingModel;

    private View mView;

    private OverScrollByListView mListView;

    private FollowFeedAdapter mAdapter;

    private ListingResponse<CaptureDetails> mDetailsListing;

    private ArrayList<CaptureDetails> mCaptureDetails;

    private String mUserId;

    private Callback mCallback;

    public RecentCapturesTabFragment() {
        // Required empty public constructor
    }

    public static RecentCapturesTabFragment newInstance(String userId) {
        RecentCapturesTabFragment fragment = new RecentCapturesTabFragment();
        Bundle args = new Bundle();
        args.putString(sArgsUserId, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        mCaptureDetails = new ArrayList<CaptureDetails>();
        Bundle args = getArguments();
        if (args != null) {
            mUserId = args.getString(sArgsUserId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.list_view_layout, container, false);

        mListView = (OverScrollByListView) mView.findViewById(android.R.id.list);

        // Not handling pagination here
        mAdapter = new FollowFeedAdapter(getActivity(), mCaptureDetails, null, this, mUserId);
        mAdapter.setCurrentViewType(FollowFeedAdapter.VIEW_TYPE_SIMPLE);

        mListView.setAdapter(mAdapter);
        mListView.setCallback(this);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocalData();
        mCaptureController.refreshUserCaptureFeed(mUserId);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private void loadLocalData() {
        new SafeAsyncTask<ListingResponse<CaptureDetails>>(this) {
            @Override
            protected ListingResponse<CaptureDetails> safeDoInBackground(Void[] params) {
                return mCaptureDetailsListingModel.getUserCaptures(mUserId);
            }

            @Override
            protected void safeOnPostExecute(ListingResponse<CaptureDetails> data) {
                mDetailsListing = data;
                mCaptureDetails.clear();

                mDetailsListing = data;
                mCaptureDetails.clear();
                if (mDetailsListing != null) {
                    mCaptureDetails.addAll(mDetailsListing.getSortedCombinedData());
                } else {
                    // TODO: Emptystate for no data?
                }
                mAdapter.notifyDataSetChanged();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onEventMainThread(UpdatedUserCaptureFeedEvent event) {
        if (!mUserId.equals(event.getAccountId())) {
            return;
        }
        if (event.isSuccessful()) {
            loadLocalData();
        } else if (event.getErrorMessage() != null) {
            showToastError(event.getErrorMessage());
        }
    }

    @Override
    public void launchWineProfile(CaptureDetails captureDetails) {
        Intent intent = new Intent();
        // Launch WineProfile if the capture matched a wine, otherwise launch the capture details
        if (captureDetails.getWineProfile() != null) {
            intent.putExtra(WineProfileActivity.PARAMS_WINE_PROFILE,
                    captureDetails.getWineProfile());
            intent.putExtra(WineProfileActivity.PARAMS_CAPTURE_PHOTO_HASH,
                    captureDetails.getPhoto());
            intent.setClass(getActivity(), WineProfileActivity.class);
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

    public interface Callback {

        /**
         * Helper callback to pass up the ListView OverScroll when the user overscrolled the top of
         * the ListView
         */
        public void onCaptureListOverScrolledTop();
    }
}
