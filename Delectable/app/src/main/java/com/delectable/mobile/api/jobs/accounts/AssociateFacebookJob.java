package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.accounts.AccountPrivateResponse;
import com.delectable.mobile.api.endpointmodels.registrations.AuthorizeFacebookRequest;
import com.delectable.mobile.api.events.accounts.AssociateFacebookEvent;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.path.android.jobqueue.Params;

public class AssociateFacebookJob extends BaseJob {

    private static final String TAG = AssociateFacebookJob.class.getSimpleName();

    private String mFacebookToken;

    private double mFacebookTokenExpiration;

    public AssociateFacebookJob(String facebookToken, double facebookTokenExpiration) {
        super(new Params(Priority.UX.value()).requireNetwork());
        mFacebookToken = facebookToken;
        mFacebookTokenExpiration = facebookTokenExpiration;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/accounts/associate_facebook";
        AuthorizeFacebookRequest request = new AuthorizeFacebookRequest(mFacebookToken,
                mFacebookTokenExpiration);
        AccountPrivateResponse response = mNetworkClient
                .post(endpoint, request, AccountPrivateResponse.class);

        Account account = response.getPayload().getAccount();
        UserInfo.setAccountPrivate(account);
        mEventBus.post(new AssociateFacebookEvent(account));
        mEventBus.post(new UpdatedAccountEvent(account));
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new AssociateFacebookEvent(TAG + " " + getErrorMessage()));
    }

}
