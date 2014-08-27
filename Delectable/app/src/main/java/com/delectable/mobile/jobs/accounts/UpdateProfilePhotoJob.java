package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.events.accounts.UpdatedProfilePhotoEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.PhotoHashResponse;
import com.delectable.mobile.model.api.accounts.AccountsUpdateProfilePhotoRequest;
import com.path.android.jobqueue.Params;

public class UpdateProfilePhotoJob extends BaseJob {

    private static final String TAG = UpdateProfilePhotoJob.class.getSimpleName();

    private String mBucket;
    private String mFilename;

    public UpdateProfilePhotoJob(ProvisionCapture provisionCapture) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mBucket = provisionCapture.getBucket();
        mFilename = provisionCapture.getFilename();
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_profile_photo";
        AccountsUpdateProfilePhotoRequest request = new AccountsUpdateProfilePhotoRequest(
                mBucket, mFilename);
        PhotoHashResponse response = getNetworkClient().post(endpoint, request,
                PhotoHashResponse.class);
        getEventBus().post(new UpdatedProfilePhotoEvent(response.getPayload().getPhoto()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedProfilePhotoEvent(TAG + " " + getErrorMessage()));
    }
}
