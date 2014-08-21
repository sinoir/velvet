package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.EditedCaptureCommentEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.api.captures.EditCommentRequest;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class EditCaptureCommentJob extends Job {

    private static final String TAG = EditCaptureCommentJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mErrorMessage;

    private String mCaptureId;

    private String mCommentId;

    private String mCaptureComment;

    public EditCaptureCommentJob(String captureId, String commentId, String captureComment) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mCaptureId = captureId;
        mCommentId = commentId;
        mCaptureComment = captureComment;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/edit_comment";

        EditCommentRequest request = new EditCommentRequest(mCaptureId, mCommentId,
                mCaptureComment);

        // Response has no payload, just "success"
        BaseResponse response = mNetworkClient.post(endpoint, request, BaseResponse.class);

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
        mEventBus.post(new EditedCaptureCommentEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        if (mErrorMessage != null) {
            mEventBus.post(new EditedCaptureCommentEvent(mErrorMessage, mCaptureId));
        } else {
            mEventBus.post(new EditedCaptureCommentEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.e(TAG + ".Error", "", throwable);
        return false;
    }

}
