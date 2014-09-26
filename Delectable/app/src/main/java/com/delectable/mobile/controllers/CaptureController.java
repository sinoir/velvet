package com.delectable.mobile.controllers;

import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.jobs.captures.AddCaptureCommentJob;
import com.delectable.mobile.jobs.captures.DeleteCaptureJob;
import com.delectable.mobile.jobs.captures.EditCaptureCommentJob;
import com.delectable.mobile.jobs.captures.FetchCaptureDetailsJob;
import com.delectable.mobile.jobs.captures.FetchCaptureNotesJob;
import com.delectable.mobile.jobs.captures.FetchFollowerFeedJob;
import com.delectable.mobile.jobs.captures.FetchUserCaptureFeedJob;
import com.delectable.mobile.jobs.captures.LikeCaptureJob;
import com.delectable.mobile.jobs.captures.MarkCaptureHelpfulJob;
import com.delectable.mobile.jobs.captures.RateCaptureJob;
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

    public void addCommentToCapture(String captureId, String captureComment) {
        mJobManager.addJobInBackground(new AddCaptureCommentJob(captureId, captureComment));
    }

    public void editCaptureComment(String captureId, String commentId, String captureComment) {
        mJobManager.addJobInBackground(
                new EditCaptureCommentJob(captureId, commentId, captureComment));
    }

    public void toggleLikeCapture(String captureId, String userId, boolean userLikesCapture) {
        mJobManager.addJobInBackground(new LikeCaptureJob(captureId, userId, userLikesCapture));
    }

    public void rateCapture(String captureId, String userId, int rating) {
        mJobManager.addJobInBackground(new RateCaptureJob(captureId, userId, rating));
    }

    public void deleteCapture(String captureId) {
        mJobManager.addJobInBackground(new DeleteCaptureJob(captureId));
    }

    public void fetchCaptureNotes(String baseWineId, String wineProfileId, Float before,
            Float after, String includeCaptureNote) {
        mJobManager.addJobInBackground(new FetchCaptureNotesJob(baseWineId, wineProfileId, before,
                after, includeCaptureNote));
    }

    public void markCaptureHelpful(CaptureNote captureNote, boolean helpful) {
        mJobManager.addJobInBackground(new MarkCaptureHelpfulJob(captureNote.getId(), helpful));
    }
}
