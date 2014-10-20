package com.delectable.mobile.jobs.registrations;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.registrations.AuthorizeFacebookRequest;
import com.delectable.mobile.api.endpointmodels.registrations.RegistrationFacebookResponse;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import java.util.Calendar;

public class LoginFacebookJob extends BaseJob {

    private static final String TAG = LoginFacebookJob.class.getSimpleName();

    private String mFacebookToken;

    private double mFacebookTokenExpiration;

    public LoginFacebookJob(String facebookToken, double facebookTokenExpiration) {
        super(new Params(Priority.UX));
        mFacebookToken = facebookToken;
        mFacebookTokenExpiration = facebookTokenExpiration;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/registrations/facebook";
        String deviceId = null; //TODO grab deviceid and pass in
        AuthorizeFacebookRequest request = new AuthorizeFacebookRequest(deviceId, mFacebookToken,
                mFacebookTokenExpiration);
        RegistrationFacebookResponse response = mNetworkClient
                .post(endpoint, request, RegistrationFacebookResponse.class, false);

        String sessionKey = response.payload.session_key;
        String sessionToken = response.payload.session_token;
        boolean newUser = response.payload.new_user;
        Account account = response.payload.account;

        UserInfo.onSignIn(account.getId(), account.getFullName(), account.getEmail(), sessionKey, sessionToken);
        UserInfo.setAccountPrivate(account);
        CrashlyticsUtil
                .onSignIn(account.getFullName(), account.getEmail(), account.getId(), sessionKey);

        mEventBus.post(new UpdatedAccountEvent(account));
        mEventBus.post(new LoginRegisterEvent(true));

        if (newUser) {
            KahunaUtil.trackSignUp("facebook", account.getFname(), account.getLname(),
                    Calendar.getInstance().getTime());
        } else {
            KahunaUtil.trackLogin(account.getId(), account.getEmail());
        }
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new LoginRegisterEvent(TAG + " " + getErrorMessage(), getErrorCode()));
    }

}
