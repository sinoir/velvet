package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.events.captures.UpdatedUserCaptureFeedEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureFeedListingRequest;
import com.delectable.mobile.model.api.captures.CaptureFeedResponse;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FetchUserCaptureFeedJob extends Job {

    private static final String TAG = FetchUserCaptureFeedJob.class.getSimpleName();

    @Inject
    CaptureDetailsListingModel mCapturesModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mAccountId;

    private boolean mIsPaginating;

    private String mErrorMessage;

    public FetchUserCaptureFeedJob(String accountId, boolean isPaginating) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mAccountId = accountId;
        mIsPaginating = isPaginating;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
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

        CaptureFeedResponse response = mNetworkClient.post(endpoint, request,
                CaptureFeedResponse.class);

        ListingResponse<CaptureDetails> captureListing = response.payload;
        // Sometimes Payload may be null, not sure why
        if (response.payload != null) {
            if (cachedCaptures != null) {
                captureListing.combineWithPreviousListing(cachedCaptures);
                captureListing.setETag(cachedCaptures.getETag());
            }
            captureListing.updateCombinedData();
            mCapturesModel.saveUserCaptures(mAccountId, captureListing);
        }
        mEventBus.post(new UpdatedUserCaptureFeedEvent(true, mAccountId));
    }

    @Override
    protected void onCancel() {
        if (mErrorMessage != null) {
            mEventBus.post(new UpdatedUserCaptureFeedEvent(mErrorMessage, mAccountId));
        } else {
            mEventBus.post(new UpdatedUserCaptureFeedEvent(false, mAccountId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.e(TAG + ".Error", "", throwable);
        return false;
    }
}
