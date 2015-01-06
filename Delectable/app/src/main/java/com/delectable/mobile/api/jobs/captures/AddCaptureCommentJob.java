package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.endpointmodels.captures.CaptureDetailsResponse;
import com.delectable.mobile.api.endpointmodels.captures.CapturesCommentRequest;
import com.delectable.mobile.api.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;

import javax.inject.Inject;

public class AddCaptureCommentJob extends BaseJob {

    private static final String TAG = AddCaptureCommentJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    private String mCaptureComment;

    private ArrayList<CaptureCommentAttributes> mCommentAttributes;

    public AddCaptureCommentJob(String captureId, String captureComment,
            ArrayList<CaptureCommentAttributes> attributes) {
        super(new Params(Priority.SYNC.value()));
        mCaptureId = captureId;
        mCaptureComment = captureComment;
        mCommentAttributes = attributes;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/comment";

        //don't run job if comment doesn't exist
        if (mCaptureComment == null || mCaptureComment.trim().isEmpty()) {
            mEventBus.post(new AddCaptureCommentEvent(false, mCaptureId));
            return;
        }

        CapturesCommentRequest request = new CapturesCommentRequest(mCaptureId, mCaptureComment,
                mCommentAttributes);

        CaptureDetailsResponse response = getNetworkClient().post(endpoint, request,
                CaptureDetailsResponse.class);

        CaptureDetails capture = response.getCapture();

        mCapturesModel.saveCaptureDetails(capture);
        getEventBus().post(new AddCaptureCommentEvent(true, mCaptureId));

        KahunaUtil.trackComment();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new AddCaptureCommentEvent(getErrorMessage(), mCaptureId,
                    getErrorCode()));
        } else {
            getEventBus().post(new AddCaptureCommentEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
