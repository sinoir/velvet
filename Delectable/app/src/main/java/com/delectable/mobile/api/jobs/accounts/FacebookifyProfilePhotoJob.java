package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.events.accounts.UpdatedProfilePhotoEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.accounts.PhotoHashResponse;
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
        PhotoHashResponse response = getNetworkClient()
                .post(endpoint, request,
                        PhotoHashResponse.class);
        getEventBus()
                .post(new UpdatedProfilePhotoEvent(response.getPayload().getPhoto()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedProfilePhotoEvent(TAG + " " + getErrorMessage()));
    }
}
