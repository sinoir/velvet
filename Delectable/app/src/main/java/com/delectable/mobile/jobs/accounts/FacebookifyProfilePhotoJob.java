package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.UpdatedProfilePhotoEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.accounts.AccountsPhotoHashResponse;
import com.path.android.jobqueue.Params;

public class FacebookifyProfilePhotoJob extends BaseJob {

    private static final String TAG = FacebookifyProfilePhotoJob.class.getSimpleName();

    public FacebookifyProfilePhotoJob() {
        super(new Params(Priority.SYNC).requireNetwork().persist());
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/facebookify_profile_photo";
        BaseRequest request = new BaseRequest(); //empty payload, so we use baserequest
        AccountsPhotoHashResponse response = getNetworkClient()
                .post(endpoint, request,
                        AccountsPhotoHashResponse.class);
        getEventBus()
                .post(new UpdatedProfilePhotoEvent(response.getPayload().getPhoto()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedProfilePhotoEvent(TAG + " " + getErrorMessage()));
    }
}
