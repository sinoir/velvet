package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.ProvisionProfilePhotoEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.accounts.AccountsProvisionProfilePhotoResponse;
import com.path.android.jobqueue.Params;

public class ProvisionProfilePhotoJob extends BaseJob {

    private static final String TAG = ProvisionProfilePhotoJob.class.getSimpleName();

    public ProvisionProfilePhotoJob() {
        super(new Params(Priority.SYNC).requireNetwork().persist());
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/provision_profile_photo";
        BaseRequest request = new BaseRequest(); //empty payload, so we use baserequest
        AccountsProvisionProfilePhotoResponse response = getNetworkClient()
                .post(endpoint, request, AccountsProvisionProfilePhotoResponse.class);
        getEventBus().post(new ProvisionProfilePhotoEvent(response.getPayload()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new ProvisionProfilePhotoEvent(TAG + " " + getErrorMessage()));
    }
}
