package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.jobs.basewines.FetchBaseWineJob;
import com.delectable.mobile.api.jobs.basewines.FetchWineSourceJob;
import com.delectable.mobile.api.jobs.basewines.SearchWinesJob;
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

    /**
     * Gets Price for Wine
     */
    public void fetchWineSource(String wineId, String state) {
        mJobManager.addJobInBackground(new FetchWineSourceJob(wineId, state));
    }
}
