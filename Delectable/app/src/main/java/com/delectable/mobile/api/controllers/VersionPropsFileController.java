package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.jobs.builddatecheck.FetchVersionPropsJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class VersionPropsFileController {

    private static final String TAG = VersionPropsFileController.class.getSimpleName();

    @Inject
    JobManager mJobManager;

    public void checkForNewBuild() {
        mJobManager.addJobInBackground(new FetchVersionPropsJob());
    }
}
