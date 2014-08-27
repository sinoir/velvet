package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.UpdatedProfileEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.api.accounts.AccountsUpdateProfileRequest;
import com.path.android.jobqueue.Params;

public class UpdateProfileJob extends BaseJob {

    private static final String TAG = UpdateProfileJob.class.getSimpleName();

    private String mFname;

    private String mLname;

    private String mUrl;

    private String mBio;

    public UpdateProfileJob(String fname, String lname, String url, String bio) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mFname = fname;
        mLname = lname;
        mUrl = url;
        mBio = bio;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_profile";
        AccountsUpdateProfileRequest request = new AccountsUpdateProfileRequest(
                mFname, mLname, mUrl, mBio);
        BaseResponse response = getNetworkClient().post(endpoint, request,
                BaseResponse.class);
        getEventBus().post(new UpdatedProfileEvent(mFname, mLname, mUrl, mBio));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedProfileEvent(TAG + " " + getErrorMessage()));
    }
}
