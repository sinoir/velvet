package com.delectable.mobile.api.jobs.registrations;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.registrations.RegisterLoginResponse;
import com.delectable.mobile.api.endpointmodels.registrations.RegistrationsLoginRequest;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import android.provider.Settings;

public class LoginJob extends BaseJob {

    private static final String TAG = LoginJob.class.getSimpleName();

    private String mEmail;

    private String mPassword;


    public LoginJob(String requestId, String email, String password) {
        super(requestId, new Params(Priority.UX.value()));
        mEmail = email;
        mPassword = password;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/registrations/login";
        //get device udid
        String deviceId = Settings.Secure
                .getString(App.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        RegistrationsLoginRequest request = new RegistrationsLoginRequest(deviceId, mEmail,
                mPassword);
        RegisterLoginResponse response = mNetworkClient
                .post(endpoint, request, RegisterLoginResponse.class, false);

        String sessionKey = response.payload.session_key;
        String sessionToken = response.payload.session_token;
        Account account = response.payload.account;

        UserInfo.onSignIn(account.getId(), account.getFullName(), account.getEmail(), sessionKey,
                sessionToken);
        UserInfo.setAccountPrivate(account);
        CrashlyticsUtil.onSignIn(account.getFullName(), account.getEmail(), account.getId(),
                sessionKey);

        mEventBus.post(new UpdatedAccountEvent(mRequestId, account));
        mEventBus.post(new LoginRegisterEvent(true));

        KahunaUtil.trackLogin(account.getId(), account.getEmail());
        mAnalytics.identify(account.getId());
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new LoginRegisterEvent(TAG + " " + getErrorMessage(), getErrorCode()));
    }

}
