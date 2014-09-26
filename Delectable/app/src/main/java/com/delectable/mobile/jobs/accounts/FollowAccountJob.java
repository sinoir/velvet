package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.App;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.FollowAccountEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.api.accounts.AccountFollowRequest;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FollowAccountJob extends BaseJob {

    private static final String TAG = FollowAccountJob.class.getSimpleName();

    @Inject
    AccountModel mAccountModel;

    private String mAccountId;

    private boolean mIsFollowing;

    private int mOriginalUserRelationship;

    public FollowAccountJob(String id, boolean follow) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mAccountId = id;
        mIsFollowing = follow;
    }

    @Override
    public void onRun() throws Throwable {
        Account followingUserAccount = mAccountModel.getAccount(mAccountId);
        Account currentUser = mAccountModel.getAccount(UserInfo.getUserId(App.getInstance()));
        String endpoint = "/accounts/follow";
        AccountFollowRequest request = new AccountFollowRequest(
                new AccountFollowRequest.AccountFollowPayload(mAccountId, mIsFollowing));
        BaseResponse response = mNetworkClient.post(endpoint, request, BaseResponse.class);
        getEventBus().post(new FollowAccountEvent(mAccountId, response.isSuccess()));

        if (response.isSuccess() && followingUserAccount != null && mIsFollowing) {
            KahunaUtil.trackFollowUser("" + currentUser.getFollowingCount(),
                    followingUserAccount.getFullName(), followingUserAccount.getId());
        }
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FollowAccountEvent(mAccountId, getErrorMessage()));
    }
}
