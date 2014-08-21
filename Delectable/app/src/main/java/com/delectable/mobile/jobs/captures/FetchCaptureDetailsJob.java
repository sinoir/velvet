package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.UpdatedCaptureDetailsEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureDetailsResponse;
import com.delectable.mobile.model.api.captures.CapturesContextRequest;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FetchCaptureDetailsJob extends Job {

    private static final String TAG = FetchCaptureDetailsJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mErrorMessage;

    private String mCaptureId;

    public FetchCaptureDetailsJob(String captureId) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mCaptureId = captureId;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/context";

        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);

        CapturesContextRequest request = new CapturesContextRequest(mCaptureId);
        if (cachedCapture != null) {
            request.setETag(cachedCapture.getETag());
        }

        CaptureDetailsResponse response = mNetworkClient
                .post(endpoint, request, CaptureDetailsResponse.class);

        CaptureDetails capture = response.payload.capture;

        mCapturesModel.saveCaptureDetails(capture);
        mEventBus.post(new UpdatedCaptureDetailsEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        if (mErrorMessage != null) {
            mEventBus.post(new UpdatedCaptureDetailsEvent(mErrorMessage, mCaptureId));
        } else {
            mEventBus.post(new UpdatedCaptureDetailsEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.e(TAG + ".Error", "", throwable);
        return false;
    }

}
