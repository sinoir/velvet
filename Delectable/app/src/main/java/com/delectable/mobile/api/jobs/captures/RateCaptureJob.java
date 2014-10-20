package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.events.captures.RatedCaptureEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.endpointmodels.captures.RateCaptureRequest;
import com.path.android.jobqueue.Params;

import java.util.HashMap;

import javax.inject.Inject;

public class RateCaptureJob extends BaseJob {

    private static final String TAG = RateCaptureJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    private String mUserId;

    private int mCaptureRating;

    public RateCaptureJob(String captureId, String userId, int captureRating) {
        super(new Params(Priority.SYNC));
        mCaptureId = captureId;
        mCaptureRating = captureRating;
        mUserId = userId;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/rate";

        RateCaptureRequest request = new RateCaptureRequest(mCaptureId, mCaptureRating);

        // Response has no payload, just "success"
        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);

        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        // Only save cached capture if cache exists
        if (cachedCapture != null) {
            if (cachedCapture.getRatings() == null) {
                cachedCapture.setRatings(new HashMap<String, Integer>());
            }
            cachedCapture.getRatings().put(mUserId, mCaptureRating);
            // Save updated rating
            mCapturesModel.saveCaptureDetails(cachedCapture);
            mCapturesModel.saveCaptureDetails(cachedCapture);
        }

        getEventBus().post(new RatedCaptureEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus()
                    .post(new RatedCaptureEvent(getErrorMessage(), mCaptureId, getErrorCode()));
        } else {
            getEventBus().post(new RatedCaptureEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
