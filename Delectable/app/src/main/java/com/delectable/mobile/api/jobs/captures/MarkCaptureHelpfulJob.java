package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.events.captures.MarkedCaptureHelpfulEvent;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.endpointmodels.ActionRequest;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.path.android.jobqueue.Params;

public class MarkCaptureHelpfulJob extends BaseJob {

    private static final String TAG = MarkCaptureHelpfulJob.class.getSimpleName();

    private String mCaptureId;

    private boolean mAction;


    public MarkCaptureHelpfulJob(String captureId, boolean action) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mCaptureId = captureId;
        mAction = action;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/helpful";
        ActionRequest request = new ActionRequest(mCaptureId, mAction);
        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);
        getEventBus().post(new MarkedCaptureHelpfulEvent(mCaptureId, response.isSuccess()));
    }

    @Override
    protected void onCancel() {
        getEventBus()
                .post(new MarkedCaptureHelpfulEvent(mCaptureId, TAG + " " + getErrorMessage()));
    }


}
