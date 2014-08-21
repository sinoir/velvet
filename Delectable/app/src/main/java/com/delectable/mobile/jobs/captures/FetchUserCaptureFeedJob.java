package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.events.captures.UpdatedUserCaptureFeedEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureFeedListingRequest;
import com.delectable.mobile.model.api.captures.CaptureFeedResponse;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchUserCaptureFeedJob extends BaseJob {

    private static final String TAG = FetchUserCaptureFeedJob.class.getSimpleName();

    @Inject
    CaptureDetailsListingModel mCapturesModel;

    private String mAccountId;

    private boolean mIsPaginating;

    public FetchUserCaptureFeedJob(String accountId, boolean isPaginating) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mAccountId = accountId;
        mIsPaginating = isPaginating;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/accounts/captures";

        CaptureFeedListingRequest request = new CaptureFeedListingRequest();
        request.setId(mAccountId);

        ListingResponse<CaptureDetails> cachedCaptures = mCapturesModel.getUserCaptures(mAccountId);
        // TODO: Check if we should invalidate / if cached response expired
        if (cachedCaptures != null) {
            request.setCurrentListing(cachedCaptures);
            request.setETag(cachedCaptures.getETag());
        }
        request.setIsPullToRefresh(!mIsPaginating);

        CaptureFeedResponse response = getNetworkClient().post(endpoint, request,
                CaptureFeedResponse.class);

        ListingResponse<CaptureDetails> captureListing = response.getPayload();
        // Sometimes Payload may be null, not sure why
        if (response.getPayload() != null) {
            if (cachedCaptures != null) {
                captureListing.combineWithPreviousListing(cachedCaptures);
                captureListing.setETag(cachedCaptures.getETag());
            }
            captureListing.updateCombinedData();
            mCapturesModel.saveUserCaptures(mAccountId, captureListing);
        }
        getEventBus().post(new UpdatedUserCaptureFeedEvent(true, mAccountId));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new UpdatedUserCaptureFeedEvent(getErrorMessage(), mAccountId));
        } else {
            getEventBus().post(new UpdatedUserCaptureFeedEvent(false, mAccountId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
