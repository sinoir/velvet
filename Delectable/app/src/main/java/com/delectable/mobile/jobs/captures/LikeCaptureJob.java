package com.delectable.mobile.jobs.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.LikedCaptureEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.ActionRequest;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class LikeCaptureJob extends Job {

    private static final String TAG = LikeCaptureJob.class.getSimpleName();

    @Inject
    CaptureDetailsModel mCapturesModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mErrorMessage;

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
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/captures/like";

        ActionRequest request = new ActionRequest(mCaptureId, mIsLiked);

        BaseResponse response = mNetworkClient.post(endpoint, request, BaseResponse.class);

        if (response.getError() != null) {
            mEventBus.post(new LikedCaptureEvent(response.getError().getMessage(), mCaptureId));
            return;
        }

        CaptureDetails cachedCapture = mCapturesModel.getCapture(mCaptureId);
        cachedCapture.toggleUserLikesCapture(mUserId);

        mCapturesModel.saveCaptureDetails(cachedCapture);
        mEventBus.post(new LikedCaptureEvent(true, mCaptureId));
    }

    @Override
    protected void onCancel() {
        if (mErrorMessage != null) {
            mEventBus.post(new LikedCaptureEvent(mErrorMessage, mCaptureId));
        } else {
            mEventBus.post(new LikedCaptureEvent(false, mCaptureId));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.e(TAG + ".Error", "", throwable);
        return false;
    }
}
