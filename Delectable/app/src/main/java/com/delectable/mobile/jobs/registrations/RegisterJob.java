package com.delectable.mobile.jobs.registrations;

import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.events.registrations.LoginRegisterEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.registrations.RegistrationLoginResponse;
import com.delectable.mobile.model.api.registrations.RegistrationsRegisterRequest;
import com.delectable.mobile.model.local.Account;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class RegisterJob extends Job {

    private static final String TAG = RegisterJob.class.getSimpleName();

    @Inject
    AccountModel mAccountModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mEmail;

    private String mPassword;

    private String mFname;

    private String mLname;

    private String mErrorMessage;


    public RegisterJob(String email, String password, String fname, String lname) {
        super(new Params(Priority.UX).requireNetwork());
        mEmail = email;
        mPassword = password;
        mFname = fname;
        mLname = lname;
    }

    @Override
    public void onAdded() {
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
        mAccountModel.saveAccount(account);
        mEventBus.post(new UpdatedAccountEvent(account.getId()));

        UserInfo.onSignIn(account.getId(), sessionKey, sessionToken);
        mEventBus.post(new LoginRegisterEvent(true));
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new LoginRegisterEvent(mErrorMessage));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        mErrorMessage = throwable.getMessage();
        Log.e(TAG + ".Error", mErrorMessage);
        return false;
    }
}
