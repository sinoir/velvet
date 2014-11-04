package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.endpointmodels.ActionRequest;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.events.captures.LikedCaptureEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.CaptureDetails;
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
        super(new Params(Priority.SYNC));
        mCaptureId = captureId;
        mUserId = userId;
        mIsLiked = isLiked;
    }

    @Override
    public void onAdded() {
        //toggle like in model
        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        cachedCapture.toggleUserLikesCapture(mUserId);
        mCapturesModel.saveCaptureDetails(cachedCapture);
        getEventBus().post(new LikedCaptureEvent(true, mCaptureId));
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/captures/like";

        ActionRequest request = new ActionRequest(mCaptureId, mIsLiked);

        // Response has no payload, just "success"
        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);

        if (mIsLiked) {
            CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
            KahunaUtil.trackLikeCapture(mCaptureId, cachedCapture.getDisplayTitle());
        }
    }

    @Override
    protected void onCancel() {

        //if fail, then we need to revert the like back to the original
        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        cachedCapture.toggleUserLikesCapture(mUserId);
        mCapturesModel.saveCaptureDetails(cachedCapture);
        getEventBus().post(new LikedCaptureEvent(getErrorMessage(), mCaptureId, getErrorCode()));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
