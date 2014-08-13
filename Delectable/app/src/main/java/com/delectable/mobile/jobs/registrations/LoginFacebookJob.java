package com.delectable.mobile.jobs.registrations;

import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.events.registrations.LoginEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.registrations.RegistrationFacebookRequest;
import com.delectable.mobile.model.api.registrations.RegistrationFacebookResponse;
import com.delectable.mobile.model.local.Account;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class LoginFacebookJob extends Job {

    private static final String TAG = LoginFacebookJob.class.getSimpleName();

    @Inject
    AccountModel mAccountModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mFacebookToken;

    private double mFacebookTokenExpiration;

    public LoginFacebookJob(String facebookToken, double facebookTokenExpiration) {
        super(new Params(Priority.UX).requireNetwork());
        mFacebookToken = facebookToken;
        mFacebookTokenExpiration = facebookTokenExpiration;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/registrations/facebook";
        RegistrationFacebookRequest request = new RegistrationFacebookRequest(
                new RegistrationFacebookRequest.RegistrationFacebookPayload(mFacebookToken,
                        mFacebookTokenExpiration));
        RegistrationFacebookResponse response = mNetworkClient
                .post(endpoint, request, RegistrationFacebookResponse.class);

        String sessionKey = response.payload.session_key;
        String sessionToken = response.payload.session_token;
        boolean newUser = response.payload.new_user;
        Account account = response.payload.account;
        mAccountModel.saveAccount(account);
        mEventBus.post(new UpdatedAccountEvent(account.getId()));

        UserInfo.onSignIn(account.getId(), sessionKey, sessionToken);
        mEventBus.post(new LoginEvent(true));
        Log.d(TAG, "FACEBOOK LOGIN: " + sessionKey + "/" + sessionToken + "/" + account.getId());

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
