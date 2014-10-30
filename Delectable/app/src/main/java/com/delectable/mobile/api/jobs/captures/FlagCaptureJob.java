package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.endpointmodels.ActionRequest;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;


public class FlagCaptureJob extends BaseJob {

    private static final String TAG = FlagCaptureJob.class.getSimpleName();

    private String mCaptureId;

    public FlagCaptureJob(String captureId) {
        super(new Params(Priority.SYNC));
        mCaptureId = captureId;
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/flag";

        ActionRequest request = new ActionRequest(mCaptureId);

        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);
    }

}
