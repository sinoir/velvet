package com.delectable.mobile.api.jobs.pendingcaptures;

import com.delectable.mobile.api.endpointmodels.pendingcaptures.SetBaseWineRequest;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.CreatePendingCaptureResponse;
import com.delectable.mobile.api.events.pendingcaptures.SetBaseWineEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

public class SetBaseWineJob extends BaseJob {

    private static final String TAG = SetBaseWineJob.class.getSimpleName();

    private String mPendingCaptureId;

    private String mBaseWineId;

    public SetBaseWineJob(String pendingCaptureId, String baseWineId) {
        super(new Params(Priority.SYNC.value()).persist());
        mPendingCaptureId = pendingCaptureId;
        mBaseWineId = baseWineId;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/pending_captures/set_base_wine_id";

        SetBaseWineRequest request = new SetBaseWineRequest(mPendingCaptureId, mBaseWineId);

        CreatePendingCaptureResponse response = getNetworkClient()
                .post(endpoint, request, CreatePendingCaptureResponse.class);

        // TODO changes to local pending captures model?

        getEventBus().post(new SetBaseWineEvent(mPendingCaptureId, mBaseWineId));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus()
                    .post(new SetBaseWineEvent(getErrorMessage(), mPendingCaptureId, mBaseWineId,
                            getErrorCode()));
        }
    }

}
