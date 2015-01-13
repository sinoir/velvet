package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.jobs.hashtags.SearchHashtagsJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class HashtagController {

    @Inject
    JobManager mJobManager;

    public void searchHashtags(String query, int offset, int limit) {
        mJobManager.addJobInBackground(new SearchHashtagsJob(query, offset, limit));
    }

}
