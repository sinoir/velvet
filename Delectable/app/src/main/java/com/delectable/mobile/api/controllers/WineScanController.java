package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.endpointmodels.scanwinelabels.AddCaptureFromPendingCaptureRequest;
import com.delectable.mobile.api.jobs.scanwinelabel.AddCaptureFromPendingCaptureJob;
import com.delectable.mobile.api.jobs.scanwinelabel.CreatePendingCaptureJob;
import com.delectable.mobile.api.jobs.scanwinelabel.IdentifyLabelJob;
import com.path.android.jobqueue.JobManager;

import android.graphics.Bitmap;

import javax.inject.Inject;

public class WineScanController {

    @Inject
    JobManager mJobManager;

    public void scanLabelInstantly(Bitmap image) {
        mJobManager.addJobInBackground(new IdentifyLabelJob(image));
    }

    public void createPendingCapture(Bitmap image, String labelScanId) {
        mJobManager.addJobInBackground(new CreatePendingCaptureJob(image, labelScanId));
    }

    public void addCaptureFromPendingCapture(AddCaptureFromPendingCaptureRequest capRequest) {
        mJobManager.addJobInBackground(new AddCaptureFromPendingCaptureJob(capRequest));
    }
}
