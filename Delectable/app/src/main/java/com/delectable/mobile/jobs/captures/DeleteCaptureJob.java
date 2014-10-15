package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.DeletedCaptureEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.ActionRequest;
import com.delectable.mobile.model.api.BaseResponse;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class DeleteCaptureJob extends BaseJob {

    private static final String TAG = DeleteCaptureJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    public DeleteCaptureJob(String captureId) {
        super(new Params(Priority.SYNC));
        mCaptureId = captureId;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/delete";

        ActionRequest request = new ActionRequest(mCaptureId);

        // Response has no payload, just "success"
        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);

        mCapturesModel.deleteCaptureDetails(mCaptureId);

        getEventBus().post(new DeletedCaptureEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus()
                    .post(new DeletedCaptureEvent(getErrorMessage(), mCaptureId, getErrorCode()));
        } else {
            getEventBus().post(new DeletedCaptureEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
