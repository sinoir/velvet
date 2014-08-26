package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.events.accounts.UpdatedProfilePhotoEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountsPhotoHashResponse;
import com.delectable.mobile.model.api.accounts.AccountsUpdateProfilePhotoRequest;
import com.path.android.jobqueue.Params;

import android.util.Log;

public class UpdateProfilePhotoJob extends BaseJob {

    private static final String TAG = UpdateProfilePhotoJob.class.getSimpleName();

    //private final ProvisionCapture mProvisionCapture;

    private String mBucket;
    private String filename;

    public UpdateProfilePhotoJob(ProvisionCapture provisionCapture) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mBucket = provisionCapture.getBucket();
        filename = provisionCapture.getFilename();
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/update_profile_photo";
        AccountsUpdateProfilePhotoRequest request = new AccountsUpdateProfilePhotoRequest(
                mBucket, filename);
        AccountsPhotoHashResponse response = getNetworkClient().post(endpoint, request,
                AccountsPhotoHashResponse.class);
        getEventBus().post(new UpdatedProfilePhotoEvent(response.getPayload().getPhoto()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedProfilePhotoEvent(TAG + " " + getErrorMessage()));
    }
}
