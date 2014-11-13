package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.events.captures.UpdatedCaptureDetailsEvent;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.endpointmodels.captures.CaptureDetailsResponse;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContextRequest;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchCaptureDetailsJob extends BaseJob {

    private static final String TAG = FetchCaptureDetailsJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    public FetchCaptureDetailsJob(String captureId) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mCaptureId = captureId;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/context";

        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);

        CapturesContextRequest request = new CapturesContextRequest(mCaptureId);
        if (cachedCapture != null) {
            request.setETag(cachedCapture.getETag());
        }

        CaptureDetailsResponse response = getNetworkClient()
                .post(endpoint, request, CaptureDetailsResponse.class);

        CaptureDetails capture = response.getCapture();

        if (capture != null) {
            mCapturesModel.saveCaptureDetails(capture);
        }
        //capture will be null if e_tag match was true

        getEventBus().post(new UpdatedCaptureDetailsEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new UpdatedCaptureDetailsEvent(getErrorMessage(), mCaptureId));
        } else {
            getEventBus().post(new UpdatedCaptureDetailsEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
