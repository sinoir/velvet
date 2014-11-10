package com.delectable.mobile.api.jobs.pendingcaptures;

import com.delectable.mobile.api.cache.CapturesPendingCapturesListingModel;
import com.delectable.mobile.api.cache.PendingCapturesModel;
import com.delectable.mobile.api.endpointmodels.ActionRequest;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.events.pendingcaptures.DeletedPendingCaptureEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.TransitionState;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;

import javax.inject.Inject;

public class DeletePendingCaptureJob extends BaseJob {

    private static final String TAG = DeletePendingCaptureJob.class.getSimpleName();

    @Inject
    protected CapturesPendingCapturesListingModel mListingModel;

    @Inject
    protected PendingCapturesModel mPendingCapturesModel;

    private String mRequestId;

    private String mAccountId;

    private String mCaptureId;

    public DeletePendingCaptureJob(String requestId, String accountId, String captureId) {
        super(new Params(Priority.SYNC));
        mRequestId = requestId;
        mAccountId = accountId;
        mCaptureId = captureId;
    }

    @Override
    public void onAdded() {
        mPendingCapturesModel.setCaptureTransitionState(mCaptureId, TransitionState.DELETING);
        //send event to notify ui
        mEventBus.post(new DeletedPendingCaptureEvent(mRequestId, mCaptureId,
                DeletedPendingCaptureEvent.State.DELETING));
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/pending_captures/delete";

        ActionRequest request = new ActionRequest(mCaptureId);

        // Response has no payload, just "success"
        BaseResponse response = getNetworkClient().post(endpoint, request, BaseResponse.class);

        mListingModel.discardCaptureFromList(mAccountId, mCaptureId);

        mEventBus.post(new DeletedPendingCaptureEvent(mRequestId, mCaptureId, DeletedPendingCaptureEvent.State.DELETED));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mPendingCapturesModel.setCaptureTransitionState(mCaptureId, TransitionState.SYNCED);
        mEventBus.post(new DeletedPendingCaptureEvent(mRequestId, getErrorMessage(), mCaptureId,
                getErrorCode()));
    }
}
