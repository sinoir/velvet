package com.delectable.mobile.api.jobs.captures;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.ActionRequest;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.events.captures.LikedCaptureEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class LikeCaptureJob extends BaseJob {

    private static final String TAG = LikeCaptureJob.class.getSimpleName();

    @Inject
    protected CaptureDetailsModel mCapturesModel;

    private String mCaptureId;

    private AccountMinimal mUserAccount;

    private boolean mIsLiked;

    private boolean mCancelJob;

    public LikeCaptureJob(String captureId, boolean isLiked) {
        super(new Params(Priority.SYNC));
        mCaptureId = captureId;
        mIsLiked = isLiked;
    }

    @Override
    public void onAdded() {
        //toggle like in model
        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        mUserAccount = UserInfo.getAccountPrivate(App.getInstance());
        boolean valueToggled = cachedCapture.toggleUserLikesCapture(mUserAccount, mIsLiked);
        if (valueToggled) {
            mCapturesModel.saveCaptureDetails(cachedCapture);
            getEventBus().post(new LikedCaptureEvent(true, mCaptureId));
        } else {
            //cancel job because the value wasn't changed, no need to ping server
            //optimization for when user clicks like button repeatedly, and the queue ends up having redundant requests
            mCancelJob = true;
        }

    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        if (mCancelJob) {
            return;
        }
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
        cachedCapture.toggleUserLikesCapture(mUserAccount, !mIsLiked);
        mCapturesModel.saveCaptureDetails(cachedCapture);
        getEventBus().post(new LikedCaptureEvent(getErrorMessage(), mCaptureId, getErrorCode()));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return super.shouldReRunOnThrowable(throwable);
    }
}
