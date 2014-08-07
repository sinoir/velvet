package com.delectable.mobile.jobs;

import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.events.FollowAccountFailedEvent;
import com.delectable.mobile.events.UpdatedAccountEvent;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.api.accounts.AccountFollowRequest;
import com.delectable.mobile.model.local.Account;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FollowAccountJob extends Job {

    private static final String TAG = FollowAccountJob.class.getSimpleName();

    @Inject
    AccountModel mAccountModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mAccountId;

    private boolean mFollow;

    private int mOriginalUserRelationship;

    public FollowAccountJob(String id, boolean follow) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mAccountId = id;
        mFollow = follow;
    }

    @Override
    public void onAdded() {
        // Update the local model to save bandwidth and provide instant UI feedback
        Account cachedAccount = mAccountModel.getAccount(mAccountId);
        mOriginalUserRelationship = cachedAccount.getCurrentUserRelationship();
        cachedAccount.setCurrentUserRelationship(
                mFollow ? Account.RELATION_TYPE_FOLLOWING : Account.RELATION_TYPE_NONE);
        mAccountModel.saveAccount(cachedAccount);
        // Inform UI that the model has changed
        mEventBus.post(new UpdatedAccountEvent(mAccountId));
        Log.d(TAG, "FOLLOW: updated local model");
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/follow";
        AccountFollowRequest request = new AccountFollowRequest(
                new AccountFollowRequest.AccountFollowPayload(mAccountId, mFollow));
        BaseResponse response = mNetworkClient.post(endpoint, request, BaseResponse.class);
        // No need to update the UI since we did that before the actual request
        Log.d(TAG, "FOLLOW: synched with backend");
    }

    @Override
    protected void onCancel() {
        // Revert local model to previous state
        Account cachedAccount = mAccountModel.getAccount(mAccountId);
        cachedAccount.setCurrentUserRelationship(mOriginalUserRelationship);
        mAccountModel.saveAccount(cachedAccount);
        // Inform UI that the model has changed
        mEventBus.post(new UpdatedAccountEvent(mAccountId));
        // Inform UI that the request has failed
        mEventBus.post(new FollowAccountFailedEvent(mAccountId));
        Log.d(TAG, "FOLLOW: job was canceled!");
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.d(TAG, "FOLLOW RERUN: " + throwable.getMessage());
        return true;
    }
}
