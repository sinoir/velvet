package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsUpdateProfileRequest;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.path.android.jobqueue.Params;

public class UpdateProfileJob extends BaseJob {

    private static final String TAG = UpdateProfileJob.class.getSimpleName();

    private String mRequestId;

    private String mFname;

    private String mLname;

    private String mUrl;

    private String mBio;

    public UpdateProfileJob(String requestId, String fname, String lname, String url, String bio) {
        super(new Params(Priority.SYNC.value()));
        mRequestId = requestId;
        mFname = fname;
        mLname = lname;
        mUrl = url;
        mBio = bio;
    }

    @Override
    public void onAdded() {
        Account account = UserInfo.getAccountPrivate();
        //hold onto original tate of account
        UserInfo.setTempAccount(account);
        account.setFname(mFname);
        account.setLname(mLname);
        account.setUrl(mUrl);
        account.setBio(mBio);
        UserInfo.setAccountPrivate(account);
        mEventBus.post(new UpdatedAccountEvent(mRequestId, account));
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_profile";
        AccountsUpdateProfileRequest request = new AccountsUpdateProfileRequest(
                mFname, mLname, mUrl, mBio);
        BaseResponse response = mNetworkClient.post(endpoint, request,
                BaseResponse.class);
        UserInfo.clearTempAccount();
    }

    @Override
    protected void onCancel() {
        //retrieve og account and set as real account again
        Account account = UserInfo.getTempAccountPrivate();
        UserInfo.setAccountPrivate(account);
        UserInfo.clearTempAccount();
        UpdatedAccountEvent event = new UpdatedAccountEvent(mRequestId, account);
        mEventBus.post(event);
    }
}
