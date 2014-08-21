package com.delectable.mobile.jobs.captures;

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

    private String mCaptureComment;

    public AddCaptureCommentJob(String captureId, String captureComment) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mCaptureId = captureId;
        mCaptureComment = captureComment;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/comment";

        CommentCaptureRequest request = new CommentCaptureRequest(mCaptureId, mCaptureComment);

        CaptureDetailsResponse response = mNetworkClient.post(endpoint, request,
                CaptureDetailsResponse.class);

        CaptureDetails capture = response.getCapturePayload();

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
