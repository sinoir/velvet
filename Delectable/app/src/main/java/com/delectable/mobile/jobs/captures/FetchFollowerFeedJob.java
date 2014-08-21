package com.delectable.mobile.jobs.captures;


import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.events.captures.UpdatedFollowerFeedEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureFeedListingRequest;
import com.delectable.mobile.model.api.captures.CaptureFeedResponse;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchFollowerFeedJob extends BaseJob {

    private static final String TAG = FetchFollowerFeedJob.class.getSimpleName();

    @Inject
    CaptureDetailsListingModel mCapturesModel;

    private boolean mIsPaginating;

    public FetchFollowerFeedJob(boolean isPaginating) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mIsPaginating = isPaginating;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
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

        CaptureFeedResponse response = getNetworkClient()
                .post(endpoint, request, CaptureFeedResponse.class);

        ListingResponse<CaptureDetails> captureListing = response.getPayload();
        // Sometimes Payload may be null, not sure why
        if (response.getPayload() != null) {
            if (cachedCaptures != null) {
                captureListing.combineWithPreviousListing(cachedCaptures);
            }

            captureListing.updateCombinedData();
            mCapturesModel.saveFollowerFeedCaptures(captureListing);
        }
        getEventBus().post(new UpdatedFollowerFeedEvent(true));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new UpdatedFollowerFeedEvent(getErrorMessage()));
        } else {
            getEventBus().post(new UpdatedFollowerFeedEvent(false));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
