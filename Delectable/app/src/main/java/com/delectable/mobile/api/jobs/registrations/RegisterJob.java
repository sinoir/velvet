package com.delectable.mobile.api.jobs.registrations;

import com.delectable.mobile.App;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.registrations.RegisterLoginResponse;
import com.delectable.mobile.api.endpointmodels.registrations.RegistrationsRegisterRequest;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.util.AnalyticsUtil;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import android.provider.Settings;

import java.util.Calendar;

public class RegisterJob extends BaseJob {

    private static final String TAG = RegisterJob.class.getSimpleName();

    private String mEmail;

    private String mPassword;

    private String mFname;

    private String mLname;

    public RegisterJob(String email, String password, String fname, String lname) {
        super(new Params(Priority.UX.value()));
        mEmail = email;
        mPassword = password;
        mFname = fname;
        mLname = lname;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/registrations/register";
        //get device udid
        String deviceId = Settings.Secure
                .getString(App.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        RegistrationsRegisterRequest request = new RegistrationsRegisterRequest(deviceId, mEmail,
                mPassword, mFname, mLname);
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

        mEventBus.post(new UpdatedAccountEvent(account));
        mEventBus.post(new LoginRegisterEvent(true));

        KahunaUtil.trackSignUp("email", account.getFname(), account.getLname(),
                Calendar.getInstance().getTime());

        mAnalytics.alias(account.getId());
        mAnalytics.trackRegister(AnalyticsUtil.ACCOUNT_EMAIL);
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new LoginRegisterEvent(TAG + " " + getErrorMessage(), getErrorCode()));
    }

}
