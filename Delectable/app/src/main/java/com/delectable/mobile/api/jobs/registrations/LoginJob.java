package com.delectable.mobile.api.jobs.registrations;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.registrations.RegistrationLoginRequest;
import com.delectable.mobile.api.endpointmodels.registrations.RegistrationLoginResponse;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

public class LoginJob extends BaseJob {

    private static final String TAG = LoginJob.class.getSimpleName();

    private String mEmail;

    private String mPassword;


    public LoginJob(String email, String password) {
        super(new Params(Priority.UX));
        mEmail = email;
        mPassword = password;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/registrations/login";
        RegistrationLoginRequest request = new RegistrationLoginRequest(
                new RegistrationLoginRequest.RegistrationLoginPayload(mEmail, mPassword));
        RegistrationLoginResponse response = mNetworkClient
                .post(endpoint, request, RegistrationLoginResponse.class, false);

        String sessionKey = response.payload.session_key;
        String sessionToken = response.payload.session_token;
        Account account = response.payload.account;

        UserInfo.onSignIn(account.getId(), account.getFullName(), account.getEmail(), sessionKey, sessionToken);
        UserInfo.setAccountPrivate(account);
        CrashlyticsUtil.onSignIn(account.getFullName(), account.getEmail(), account.getId(),
                sessionKey);

        mEventBus.post(new UpdatedAccountEvent(account));
        mEventBus.post(new LoginRegisterEvent(true));

        KahunaUtil.trackLogin(account.getId(), account.getEmail());
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new LoginRegisterEvent(TAG + " " + getErrorMessage(), getErrorCode()));
    }

}
