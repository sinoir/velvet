package com.delectable.mobile.controllers;

import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.jobs.captures.AddCaptureCommentJob;
import com.delectable.mobile.jobs.captures.FetchCaptureDetailsJob;
import com.delectable.mobile.jobs.captures.FetchFollowerFeedJob;
import com.delectable.mobile.jobs.captures.FetchUserCaptureFeedJob;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class CaptureController {

    @Inject
    JobManager mJobManager;

    public void fetchCapture(String captureId) {
        mJobManager.addJobInBackground(new FetchCaptureDetailsJob(captureId));
    }

    public void refreshUserCaptureFeed(String userId) {
        mJobManager.addJobInBackground(new FetchUserCaptureFeedJob(userId, false));
    }

    public void paginateUserCaptureFeed(String userId) {
        mJobManager.addJobInBackground(new FetchUserCaptureFeedJob(userId, true));
    }

    public void refreshFollowerFeed() {
        mJobManager.addJobInBackground(new FetchFollowerFeedJob(false));
    }

    public void paginateFollowerFeed() {
        mJobManager.addJobInBackground(new FetchFollowerFeedJob(true));
    }

    public void addCommentToCapture(String captureId, CaptureComment captureComment) {
        mJobManager.addJobInBackground(new AddCaptureCommentJob(captureId, captureComment));
    }
}
