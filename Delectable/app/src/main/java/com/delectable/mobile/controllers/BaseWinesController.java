package com.delectable.mobile.controllers;

import com.delectable.mobile.jobs.basewines.SearchWinesJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class BaseWinesController {

    @Inject
    JobManager mJobManager;

    public void searchWine(String wine, int offset, int limit) {
        mJobManager.addJobInBackground(new SearchWinesJob(wine, offset, limit));
    }
}
