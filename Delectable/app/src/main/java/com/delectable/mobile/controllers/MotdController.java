package com.delectable.mobile.controllers;

import com.delectable.mobile.jobs.foursquare.SearchFoursquareVenuesJob;
import com.delectable.mobile.jobs.motd.FetchMotdJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class MotdController {

    private static final String TAG = MotdController.class.getSimpleName();

    @Inject
    JobManager mJobManager;

    public void getMotd(String sessionKey, String deviceId) {
        mJobManager.addJobInBackground(new FetchMotdJob(sessionKey, deviceId));
    }
}
