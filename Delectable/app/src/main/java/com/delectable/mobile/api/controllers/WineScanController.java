package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.jobs.scanwinelabel.AddCaptureFromPendingCaptureJob;
import com.delectable.mobile.api.jobs.scanwinelabel.CreatePendingCaptureJob;
import com.delectable.mobile.api.jobs.scanwinelabel.IdentifyLabelJob;
import com.delectable.mobile.api.endpointmodels.scanwinelabels.AddCaptureFromPendingCaptureRequest;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class WineScanController {

    @Inject
    JobManager mJobManager;

    public void scanLabelInstantly(byte[] imageData) {
        mJobManager.addJobInBackground(new IdentifyLabelJob(imageData));
    }

    public void createPendingCapture(byte[] imageData, String labelScanId) {
        mJobManager.addJobInBackground(new CreatePendingCaptureJob(imageData, labelScanId));
    }

    public void addCaptureFromPendingCapture(AddCaptureFromPendingCaptureRequest capRequest) {
        mJobManager.addJobInBackground(new AddCaptureFromPendingCaptureJob(capRequest));
    }
}
