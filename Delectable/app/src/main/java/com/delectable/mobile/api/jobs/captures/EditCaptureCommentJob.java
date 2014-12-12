package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.endpointmodels.captures.CapturesEditCommentRequest;
import com.delectable.mobile.api.events.captures.EditedCaptureCommentEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class EditCaptureCommentJob extends BaseJob {

    private static final String TAG = EditCaptureCommentJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    private String mCommentId;

    private String mCaptureComment;

    public EditCaptureCommentJob(String captureId, String commentId, String captureComment) {
        super(new Params(Priority.SYNC.value()));
        mCaptureId = captureId;
        mCommentId = commentId;
        mCaptureComment = captureComment;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/edit_comment";

        CapturesEditCommentRequest request = new CapturesEditCommentRequest(mCaptureId, mCommentId,
                mCaptureComment);

        // Response has no payload, just "success"
        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);

        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        if (cachedCapture.getComments() != null) {
            // Replace old comment with new comment
            CaptureComment existingComment = cachedCapture.getComment(mCommentId);
            if (existingComment != null) {
                existingComment.setComment(mCaptureComment);
            }
        }
        // Save updated comment
        mCapturesModel.saveCaptureDetails(cachedCapture);

        mCapturesModel.saveCaptureDetails(cachedCapture);
        getEventBus().post(new EditedCaptureCommentEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new EditedCaptureCommentEvent(getErrorMessage(), mCaptureId,
                    getErrorCode()));
        } else {
            getEventBus().post(new EditedCaptureCommentEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
