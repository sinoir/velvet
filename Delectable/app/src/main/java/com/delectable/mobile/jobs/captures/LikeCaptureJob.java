package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.LikedCaptureEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.ActionRequest;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class LikeCaptureJob extends BaseJob {

    private static final String TAG = LikeCaptureJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    private String mUserId;

    private boolean mIsLiked;

    public LikeCaptureJob(String captureId, String userId, boolean isLiked) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mCaptureId = captureId;
        mUserId = userId;
        mIsLiked = isLiked;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/like";

        ActionRequest request = new ActionRequest(mCaptureId, mIsLiked);

        // Response has no payload, just "success"
        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);

        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        cachedCapture.toggleUserLikesCapture(mUserId);

        mCapturesModel.saveCaptureDetails(cachedCapture);
        getEventBus().post(new LikedCaptureEvent(true, mCaptureId));

        if (mIsLiked) {
            KahunaUtil.trackLikeCapture(mCaptureId, cachedCapture.getDisplayTitle());
        }
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (getErrorMessage() != null) {
            getEventBus().post(new LikedCaptureEvent(getErrorMessage(), mCaptureId));
        } else {
            getEventBus().post(new LikedCaptureEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
