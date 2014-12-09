package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.accounts.PhotoHashResponse;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.path.android.jobqueue.Params;

public class FacebookifyProfilePhotoJob extends BaseJob {

    private static final String TAG = FacebookifyProfilePhotoJob.class.getSimpleName();

    public FacebookifyProfilePhotoJob(String requestId) {
        super(requestId, new Params(Priority.SYNC.value()));
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/facebookify_profile_photo";
        BaseRequest request = new BaseRequest(); //empty payload, so we use baserequest
        PhotoHashResponse response = getNetworkClient()
                .post(endpoint, request,
                        PhotoHashResponse.class);
        Account account = UserInfo.getAccountPrivate();
        account.setPhoto(response.getPayload().getPhoto());
        UserInfo.setAccountPrivate(account);
        mEventBus.post(new UpdatedAccountEvent(mRequestId, account));
    }

    @Override
    protected void onCancel() {
        Account account = UserInfo.getAccountPrivate();
        mEventBus.post(new UpdatedAccountEvent(mRequestId, account, TAG + " " + getErrorMessage()));
    }
}
