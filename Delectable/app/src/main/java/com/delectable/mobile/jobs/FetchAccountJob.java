package com.delectable.mobile.jobs;

import android.util.Log;

import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.events.FetchAccountFailedEvent;
import com.delectable.mobile.events.FetchedAccountEvent;
import com.delectable.mobile.model.api.AccountContextRequest;
import com.delectable.mobile.model.api.AccountContextResponse;
import com.delectable.mobile.model.local.Account;
import com.delectable.mobile.net.NetworkClient;
import com.google.gson.Gson;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FetchAccountJob extends Job {

    private static final String TAG = FetchAccountJob.class.getSimpleName();

    @Inject
    AccountModel mAccountModel;
    @Inject
    EventBus mEventBus;
    @Inject
    NetworkClient mNetworkClient;
    @Inject
    Gson mGson;

    private String mAccountId;
    private boolean mIsPrivate;

    public FetchAccountJob(String id, boolean isPrivate) {
        super(new Params(Priority.UX).requireNetwork());
        mAccountId = id;
        mIsPrivate = isPrivate;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {

        // TODO get eTag from cached instance, handle eTag match

        String endpoint = "/accounts/context";
        AccountContextRequest request = new AccountContextRequest(mIsPrivate ? "private" : "profile", new AccountContextRequest.AccountContextPayload(mAccountId));
        AccountContextResponse response = mNetworkClient.post(endpoint, request, AccountContextResponse.class);
        Account account = response.payload.account;
        Log.d(TAG, "ACCOUNT RESPONSE: " + mGson.toJson(response));

        mAccountModel.saveAccount(account);
        mEventBus.post(new FetchedAccountEvent(account.getId()));

    }

    @Override
    protected void onCancel() {
        mEventBus.post(new FetchAccountFailedEvent(mAccountId));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        // TODO check error type and see if a retry makes sense
        Log.d(TAG, "ERROR: " + throwable.getMessage());
        return true;
    }
}
