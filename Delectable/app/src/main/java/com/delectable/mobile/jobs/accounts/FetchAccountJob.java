package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.events.accounts.FetchAccountFailedEvent;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountContextRequest;
import com.delectable.mobile.model.api.accounts.AccountContextResponse;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import android.util.Log;

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

        String eTag = null;
        Account cachedAccount = mAccountModel.getAccount(mAccountId);
        if (cachedAccount != null) {
            eTag = cachedAccount.getETag();
        }

        String endpoint = "/accounts/context";
        AccountContextRequest request = new AccountContextRequest(
                mIsPrivate ? "private" : "profile", eTag,
                new AccountContextRequest.AccountContextPayload(mAccountId));
        AccountContextResponse response = mNetworkClient
                .post(endpoint, request, AccountContextResponse.class);

        if (!response.isETagMatch()) {
            Account account = response.payload.account;
            mAccountModel.saveAccount(account);
            mEventBus.post(new UpdatedAccountEvent(account.getId()));
        }

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
