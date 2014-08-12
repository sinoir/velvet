package com.delectable.mobile.jobs.registrations;

import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.events.registrations.LoginEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.registrations.RegistrationLoginRequest;
import com.delectable.mobile.model.api.registrations.RegistrationLoginResponse;
import com.delectable.mobile.model.local.Account;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class LoginJob extends Job {

    private static final String TAG = LoginJob.class.getSimpleName();

    @Inject
    AccountModel mAccountModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mEmail;

    private String mPassword;

    public LoginJob(String email, String password) {
        super(new Params(Priority.UX).requireNetwork());
        mEmail = email;
        mPassword = password;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/registrations/login";
        RegistrationLoginRequest request = new RegistrationLoginRequest(
                new RegistrationLoginRequest.RegistrationLoginPayload(mEmail, mPassword));
        RegistrationLoginResponse response = mNetworkClient
                .post(endpoint, request, RegistrationLoginResponse.class);

        String sessionKey = response.payload.session_key;
        String sessionToken = response.payload.session_token;
        Account account = response.payload.account;
        mAccountModel.saveAccount(account);
        mEventBus.post(new UpdatedAccountEvent(account.getId()));

        UserInfo.onSignIn(account.getId(), sessionKey, sessionToken);
        mEventBus.post(new LoginEvent(true));
        Log.d(TAG, "LOGIN: " + sessionKey + "/" + sessionToken + "/" + account.getId());

    }

    @Override
    protected void onCancel() {
        mEventBus.post(new LoginEvent(false));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.d(TAG, "ERROR: " + throwable.getMessage());
        return true;
    }
}