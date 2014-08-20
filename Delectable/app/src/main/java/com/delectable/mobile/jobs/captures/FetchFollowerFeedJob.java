package com.delectable.mobile.jobs.captures;


import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.events.captures.UpdatedFollowerFeedEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureFeedListingRequest;
import com.delectable.mobile.model.api.captures.CaptureFeedResponse;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FetchFollowerFeedJob extends Job {

    private static final String TAG = FetchFollowerFeedJob.class.getSimpleName();

    @Inject
    CaptureDetailsListingModel mCapturesModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private boolean mIsPaginating;

    private String mErrorMessage;

    public FetchFollowerFeedJob(boolean isPaginating) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mIsPaginating = isPaginating;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/follower_feed";

        CaptureFeedListingRequest request = new CaptureFeedListingRequest();

        ListingResponse<CaptureDetails> cachedCaptures = mCapturesModel
                .getFollowerFeedCaptures();
        // TODO: Check if we should invalidate / if cached response expired
        if (cachedCaptures != null) {
            request.setCurrentListing(cachedCaptures);
            request.setETag(cachedCaptures.getETag());
        }
        request.setIsPullToRefresh(!mIsPaginating);

        CaptureFeedResponse response = mNetworkClient
                .post(endpoint, request, CaptureFeedResponse.class);

        if (response.getError() != null) {
            mEventBus.post(new UpdatedFollowerFeedEvent(response.getError().getMessage()));
            return;
        }

        ListingResponse<CaptureDetails> captureListing = response.payload;
        // Sometimes Payload may be null, not sure why
        if (response.payload != null) {
            if (cachedCaptures != null) {
                captureListing.combineWithPreviousListing(cachedCaptures);
            }

            captureListing.updateCombinedData();
            mCapturesModel.saveFollowerFeedCaptures(captureListing);
        }
        mEventBus.post(new UpdatedFollowerFeedEvent(true));
    }

    @Override
    protected void onCancel() {
        if (mErrorMessage != null) {
            mEventBus.post(new UpdatedFollowerFeedEvent(mErrorMessage));
        } else {
            mEventBus.post(new UpdatedFollowerFeedEvent(false));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        mErrorMessage = throwable.getMessage();
        Log.e(TAG + ".Error", mErrorMessage);
        return false;
    }
}
