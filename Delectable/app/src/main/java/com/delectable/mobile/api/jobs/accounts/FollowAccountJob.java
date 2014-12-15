package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.AccountModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsFollowRequest;
import com.delectable.mobile.api.events.accounts.FollowAccountEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

public class FollowAccountJob extends BaseJob {

    private static final String TAG = FollowAccountJob.class.getSimpleName();

    private static final int ADD_ONE = 1;

    private static final int SUBTRACT_ONE = -1;

    @Inject
    AccountModel mAccountModel;

    private String mAccountId;

    private boolean mIsFollowing;

    private int mOriginalFollowerCount;

    private int mOriginalUserRelationship;

    private int mLoggedInUserOriginalFollowingCount;

    public FollowAccountJob(String id, boolean follow) {
        super(new Params(Priority.SYNC.value()));
        mAccountId = id;
        mIsFollowing = follow;
    }

    @Override
    public void onAdded() {
        Log.d(TAG, "onAdded");
        // Update accountToFollow Counts
        AccountProfile accountToFollow = mAccountModel.getAccount(mAccountId);
        if (accountToFollow != null) {
            //hold onto original values in case call fails
            mOriginalFollowerCount = accountToFollow.getFollowerCount();
            mOriginalUserRelationship = accountToFollow.getCurrentUserRelationship();

            //this can be null from search or follow friends, because we don't save information there
            int followerCountDiff = mIsFollowing ? ADD_ONE : SUBTRACT_ONE;
            accountToFollow.setFollowerCount(mOriginalFollowerCount + followerCountDiff);
            int relationship = mIsFollowing ? AccountProfile.RELATION_TYPE_FOLLOWING
                    : AccountProfile.RELATION_TYPE_NONE;
            accountToFollow.setCurrentUserRelationship(relationship);
            mAccountModel.saveAccount(accountToFollow);
        }

        // Update Signed In User Account Counts
        Account loggedInUser = UserInfo.getAccountPrivate(App.getInstance());
        mLoggedInUserOriginalFollowingCount = loggedInUser.getFollowingCount();
        int countChange = mIsFollowing ? ADD_ONE : SUBTRACT_ONE;
        loggedInUser.setFollowingCount(mLoggedInUserOriginalFollowingCount + countChange);
        UserInfo.setAccountPrivate(loggedInUser);

        AccountProfile loggedIdUserAccount = mAccountModel.getAccount(loggedInUser.getId());
        if (loggedIdUserAccount != null) {
            loggedIdUserAccount
                    .setFollowingCount(mLoggedInUserOriginalFollowingCount + countChange);
            mAccountModel.saveAccount(loggedIdUserAccount);
        }
    }

    @Override
    public void onRun() throws Throwable {

        Account currentUser = UserInfo.getAccountPrivate(App.getInstance());
        String endpoint = "/accounts/follow";
        AccountsFollowRequest request = new AccountsFollowRequest(mAccountId, mIsFollowing);
        BaseResponse response = mNetworkClient.post(endpoint, request, BaseResponse.class);

        mEventBus.post(new FollowAccountEvent(mAccountId, response.isSuccess()));

        //Kahuna tracking
        AccountMinimal accountToFollow = mAccountModel.getAccount(mAccountId);
        if (accountToFollow == null) {
            accountToFollow = mAccountModel.getAccountMinimal(mAccountId);
        }
        if (accountToFollow != null && mIsFollowing) {
            KahunaUtil.trackFollowUser("" + currentUser.getFollowingCount(),
                    accountToFollow.getFullName(), accountToFollow.getId());
        }
    }

    @Override
    protected void onCancel() {
        //revert back to original state
        AccountProfile accountToFollow = mAccountModel.getAccount(mAccountId);
        if (accountToFollow != null) {
            accountToFollow.setFollowerCount(mOriginalFollowerCount);
            accountToFollow.setCurrentUserRelationship(mOriginalUserRelationship);
            mAccountModel.saveAccount(accountToFollow);
        }

        //revert logged in user counts
        Account loggedInUser = UserInfo.getAccountPrivate(App.getInstance());
        loggedInUser.setFollowingCount(mLoggedInUserOriginalFollowingCount);
        UserInfo.setAccountPrivate(loggedInUser);

        AccountProfile loggedIdUserAccount = mAccountModel.getAccount(loggedInUser.getId());
        if (loggedIdUserAccount != null) {
            loggedIdUserAccount.setFollowingCount(mLoggedInUserOriginalFollowingCount);
            mAccountModel.saveAccount(loggedIdUserAccount);
        }

        mEventBus.post(new FollowAccountEvent(mAccountId, TAG + " " + getErrorMessage(),
                getErrorCode()));
    }
}
