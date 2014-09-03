package com.delectable.mobile.controllers;

import com.delectable.mobile.jobs.wines.FetchBaseWineJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class BaseWineController {

    @Inject
    JobManager mJobManager;

    public void fetchBaseWine(String baseWineId) {
        mJobManager.addJobInBackground(new FetchBaseWineJob(baseWineId));
    }

}
