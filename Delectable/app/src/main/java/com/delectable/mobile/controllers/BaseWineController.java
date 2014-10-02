package com.delectable.mobile.controllers;

import com.delectable.mobile.jobs.basewines.SearchWinesJob;
import com.delectable.mobile.jobs.basewines.FetchBaseWineJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class BaseWineController {

    @Inject
    JobManager mJobManager;

    public void fetchBaseWine(String baseWineId) {
        mJobManager.addJobInBackground(new FetchBaseWineJob(baseWineId));
    }

    public void searchWine(String query, int offset, int limit) {
        mJobManager.addJobInBackground(new SearchWinesJob(query, offset, limit));
    }

}
