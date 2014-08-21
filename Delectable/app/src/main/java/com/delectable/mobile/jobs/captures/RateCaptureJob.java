package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.RatedCaptureEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.api.captures.RateCaptureRequest;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import java.util.HashMap;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class RateCaptureJob extends Job {

    private static final String TAG = RateCaptureJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mErrorMessage;

    private String mCaptureId;

    private String mUserId;

    private int mCaptureRating;

    public RateCaptureJob(String captureId, String userId, int captureRating) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mCaptureId = captureId;
        mCaptureRating = captureRating;
        mUserId = userId;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/rate";

        RateCaptureRequest request = new RateCaptureRequest(mCaptureId, mCaptureRating);

        BaseResponse response = mNetworkClient.post(endpoint, request, BaseResponse.class);

        if (response.getError() != null) {
            mEventBus.post(new RatedCaptureEvent(response.getError().getMessage(),
                    mCaptureId));
            return;
        }

        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        if (cachedCapture.getRatings() == null) {
            cachedCapture.setRatings(new HashMap<String, Integer>());
        }
        cachedCapture.getRatings().put(mUserId, mCaptureRating);
        // Save updated rating
        mCapturesModel.saveCaptureDetails(cachedCapture);

        mCapturesModel.saveCaptureDetails(cachedCapture);
        mEventBus.post(new RatedCaptureEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        if (mErrorMessage != null) {
            mEventBus.post(new RatedCaptureEvent(mErrorMessage, mCaptureId));
        } else {
            mEventBus.post(new RatedCaptureEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.e(TAG + ".Error", "", throwable);
        return false;
    }

}
