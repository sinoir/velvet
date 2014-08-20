package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureDetailsResponse;
import com.delectable.mobile.model.api.captures.CommentCaptureRequest;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class AddCaptureCommentJob extends Job {

    private static final String TAG = AddCaptureCommentJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mErrorMessage;

    private String mCaptureId;

    private CaptureComment mCaptureComment;

    public AddCaptureCommentJob(String captureId, CaptureComment captureComment) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mCaptureId = captureId;
        mCaptureComment = captureComment;
    }

    @Override
    public void onAdded() {
        // Update local model
        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        if (cachedCapture.getComments() == null) {
            cachedCapture.setComments(new ArrayList<CaptureComment>());
        }
        cachedCapture.getComments().add(mCaptureComment);
        mCapturesModel.saveCaptureDetails(cachedCapture);
        mEventBus.post(new AddCaptureCommentEvent(true, mCaptureId));
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/comment";

        CommentCaptureRequest request = new CommentCaptureRequest(mCaptureId,
                mCaptureComment.getComment());

        CaptureDetailsResponse response = mNetworkClient
                .post(endpoint, request, CaptureDetailsResponse.class);

        if (response.getError() != null) {
            mEventBus.post(new AddCaptureCommentEvent(response.getError().getMessage(),
                    mCaptureId));
            return;
        }

        CaptureDetails capture = response.payload.capture;

        mCapturesModel.saveCaptureDetails(capture);
        mEventBus.post(new AddCaptureCommentEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        if (mErrorMessage != null) {
            mEventBus.post(new AddCaptureCommentEvent(mErrorMessage, mCaptureId));
        } else {
            mEventBus.post(new AddCaptureCommentEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.e(TAG + ".Error", "", throwable);
        return false;
    }

}
