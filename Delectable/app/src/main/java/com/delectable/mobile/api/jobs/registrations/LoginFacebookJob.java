package com.delectable.mobile.api.jobs.registrations;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.registrations.RegistrationsFacebookResponse;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.endpointmodels.registrations.AuthorizeFacebookRequest;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import android.provider.Settings;

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
        //get device udid
        String deviceId = Settings.Secure.getString(App.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        AuthorizeFacebookRequest request = new AuthorizeFacebookRequest(deviceId, mFacebookToken,
                mFacebookTokenExpiration);
        RegistrationsFacebookResponse response = mNetworkClient
                .post(endpoint, request, RegistrationsFacebookResponse.class, false);

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
