package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.jobs.captures.AddCaptureCommentJob;
import com.delectable.mobile.api.jobs.captures.DeleteCaptureJob;
import com.delectable.mobile.api.jobs.captures.EditCaptureCommentJob;
import com.delectable.mobile.api.jobs.captures.FetchCaptureDetailsJob;
import com.delectable.mobile.api.jobs.captures.FetchCaptureListJob;
import com.delectable.mobile.api.jobs.captures.FetchCaptureNotesJob;
import com.delectable.mobile.api.jobs.captures.FlagCaptureJob;
import com.delectable.mobile.api.jobs.captures.LikeCaptureJob;
import com.delectable.mobile.api.jobs.captures.MarkCaptureHelpfulJob;
import com.delectable.mobile.api.jobs.captures.RateCaptureJob;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.Listing;
import com.path.android.jobqueue.JobManager;

import javax.inject.Inject;

public class CaptureController {

    @Inject
    JobManager mJobManager;

    public void fetchCapture(String captureId) {
        mJobManager.addJobInBackground(new FetchCaptureDetailsJob(captureId));
    }

    public void addCommentToCapture(String captureId, String captureComment) {
        mJobManager.addJobInBackground(new AddCaptureCommentJob(captureId, captureComment));
    }

    public void editCaptureComment(String captureId, String commentId, String captureComment) {
        mJobManager.addJobInBackground(
                new EditCaptureCommentJob(captureId, commentId, captureComment));
    }

    public void toggleLikeCapture(String captureId, boolean userLikesCapture) {
        mJobManager.addJobInBackground(new LikeCaptureJob(captureId, userLikesCapture));
    }

    public void rateCapture(String captureId, String userId, int rating) {
        mJobManager.addJobInBackground(new RateCaptureJob(captureId, userId, rating));
    }

    public void deleteCapture(String captureId) {
        mJobManager.addJobInBackground(new DeleteCaptureJob(captureId));
    }

    public void fetchCaptureNotes(String requestId, String baseWineId, String wineProfileId,
            Listing<CaptureNote, String> listing, String includeCaptureNote,
            Boolean isPullToRefresh) {
        mJobManager.addJobInBackground(
                new FetchCaptureNotesJob(requestId, baseWineId, wineProfileId, listing,
                        includeCaptureNote, isPullToRefresh));
    }

    public void markCaptureHelpful(CaptureNote captureNote, boolean helpful) {
        mJobManager.addJobInBackground(new MarkCaptureHelpfulJob(captureNote.getId(), helpful));
    }

    /**
     * @param requestId       Unique identifier for Event callback.
     * @param listKey         List identifier
     * @param listing         The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public void fetchCaptureList(String requestId, String listKey,
            Listing<CaptureDetails, String> listing, Boolean isPullToRefresh) {
        mJobManager.addJobInBackground(
                new FetchCaptureListJob(requestId, listKey, listing, isPullToRefresh));
    }

    public void flagCapture(String captureId) {
        mJobManager.addJobInBackground(new FlagCaptureJob(captureId));
    }

}
