package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.captures.CaptureDetailsResponse;
import com.delectable.mobile.model.api.captures.CommentCaptureRequest;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class AddCaptureCommentJob extends BaseJob {

    private static final String TAG = AddCaptureCommentJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    private String mCaptureComment;

    public AddCaptureCommentJob(String captureId, String captureComment) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mCaptureId = captureId;
        mCaptureComment = captureComment;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/comment";

        CommentCaptureRequest request = new CommentCaptureRequest(mCaptureId, mCaptureComment);

        CaptureDetailsResponse response = getNetworkClient().post(endpoint, request,
                CaptureDetailsResponse.class);

        CaptureDetails capture = response.getCapturePayload();

        mCapturesModel.saveCaptureDetails(capture);
        getEventBus().post(new AddCaptureCommentEvent(true, mCaptureId));

        KahunaUtil.trackComment();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new AddCaptureCommentEvent(getErrorMessage(), mCaptureId));
        } else {
            getEventBus().post(new AddCaptureCommentEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
