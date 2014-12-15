package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.jobs.pendingcaptures.DeletePendingCaptureJob;
import com.delectable.mobile.api.jobs.pendingcaptures.SetBaseWineJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class PendingCapturesController {

    @Inject
    protected JobManager mJobManager;

    public void deleteCapture(String requestId, String accountId, String captureId) {
        mJobManager.addJobInBackground(new DeletePendingCaptureJob(requestId, accountId, captureId));
    }

    public void setBaseWineId(String pendingCaptureId, String baseWineId) {
        mJobManager.addJobInBackground(new SetBaseWineJob(pendingCaptureId, baseWineId));
    }

}
