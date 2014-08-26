package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.FollowAccountEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.api.accounts.AccountFollowRequest;
import com.path.android.jobqueue.Params;

public class FollowAccountJob extends BaseJob {

    private static final String TAG = FollowAccountJob.class.getSimpleName();

    private String mAccountId;

    private boolean mFollow;

    private int mOriginalUserRelationship;

    public FollowAccountJob(String id, boolean follow) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mAccountId = id;
        mFollow = follow;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/follow";
        AccountFollowRequest request = new AccountFollowRequest(
                new AccountFollowRequest.AccountFollowPayload(mAccountId, mFollow));
        BaseResponse response = mNetworkClient.post(endpoint, request, BaseResponse.class);
        getEventBus().post(new FollowAccountEvent(mAccountId, response.isSuccess()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FollowAccountEvent(mAccountId, getErrorMessage()));
    }
}
