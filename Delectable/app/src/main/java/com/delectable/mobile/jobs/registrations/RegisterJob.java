package com.delectable.mobile.jobs.registrations;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.registrations.RegistrationLoginResponse;
import com.delectable.mobile.model.api.registrations.RegistrationsRegisterRequest;
import com.delectable.mobile.net.NetworkClient;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import java.util.Calendar;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class RegisterJob extends BaseJob {

    private static final String TAG = RegisterJob.class.getSimpleName();

    private String mEmail;

    private String mPassword;

    private String mFname;

    private String mLname;

    public RegisterJob(String email, String password, String fname, String lname) {
        super(new Params(Priority.UX).requireNetwork());
        mEmail = email;
        mPassword = password;
        mFname = fname;
        mLname = lname;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/registrations/register";
        RegistrationsRegisterRequest.Payload payload = new RegistrationsRegisterRequest.Payload(
                mEmail, mPassword, mFname, mLname);
        RegistrationsRegisterRequest request = new RegistrationsRegisterRequest(payload);
        RegistrationLoginResponse response = mNetworkClient
                .post(endpoint, request, RegistrationLoginResponse.class, false);

        String sessionKey = response.payload.session_key;
        String sessionToken = response.payload.session_token;
        Account account = response.payload.account;

        UserInfo.onSignIn(account.getId(), sessionKey, sessionToken, account.getEmail());
        UserInfo.setAccountPrivate(account);
        CrashlyticsUtil.onSignIn(account.getFullName(), account.getEmail(), account.getId(),
                sessionKey);


        mEventBus.post(new UpdatedAccountEvent(account));
        mEventBus.post(new LoginRegisterEvent(true));

        KahunaUtil.trackSignUp("email", account.getFname(), account.getLname(),
                Calendar.getInstance().getTime());
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new LoginRegisterEvent(TAG + " " + getErrorMessage()));
    }

}
