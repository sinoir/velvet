package com.delectable.mobile.api.controllers;

import com.delectable.mobile.api.jobs.captures.DeleteCaptureJob;
import com.delectable.mobile.api.jobs.captures.EditCaptureCommentJob;
import com.delectable.mobile.api.jobs.captures.FetchCaptureDetailsJob;
import com.delectable.mobile.api.jobs.captures.FetchCaptureNotesJob;
import com.delectable.mobile.api.jobs.captures.LikeCaptureJob;
import com.delectable.mobile.api.jobs.captures.MarkCaptureHelpfulJob;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.jobs.captures.AddCaptureCommentJob;
import com.delectable.mobile.api.jobs.captures.FetchTrendingCapturesJob;
import com.delectable.mobile.api.jobs.captures.RateCaptureJob;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContext;
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

    public void toggleLikeCapture(String captureId, String userId, boolean userLikesCapture) {
        mJobManager.addJobInBackground(new LikeCaptureJob(captureId, userId, userLikesCapture));
    }

    public void rateCapture(String captureId, String userId, int rating) {
        mJobManager.addJobInBackground(new RateCaptureJob(captureId, userId, rating));
    }

    public void deleteCapture(String captureId) {
        mJobManager.addJobInBackground(new DeleteCaptureJob(captureId));
    }

    public void fetchCaptureNotes(String baseWineId, String wineProfileId, String before,
            String after, String includeCaptureNote) {
        mJobManager.addJobInBackground(new FetchCaptureNotesJob(baseWineId, wineProfileId, before,
                after, includeCaptureNote));
    }

    public void markCaptureHelpful(CaptureNote captureNote, boolean helpful) {
        mJobManager.addJobInBackground(new MarkCaptureHelpfulJob(captureNote.getId(), helpful));
    }

    /**
     * @param requestId       Unique identifier for Event callback.
     * @param context         Context type for capture
     * @param listing         The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public void fetchTrendingCaptures(String requestId, CapturesContext context,
            Listing<CaptureDetails> listing, Boolean isPullToRefresh) {
        mJobManager.addJobInBackground(
                new FetchTrendingCapturesJob(requestId, context, listing, isPullToRefresh));
    }
}
