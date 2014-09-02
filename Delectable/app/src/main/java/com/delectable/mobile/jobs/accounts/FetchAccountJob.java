package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountContextRequest;
import com.delectable.mobile.model.api.accounts.AccountPrivateResponse;
import com.delectable.mobile.net.NetworkClient;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FetchAccountJob extends BaseJob {

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
        AccountPrivateResponse response = mNetworkClient
                .post(endpoint, request, AccountPrivateResponse.class);

        if (!response.isETagMatch()) {
            Account account = response.getPayload().getAccount();
            mAccountModel.saveAccount(account);
            mEventBus.post(new UpdatedAccountEvent(account));
        }
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new UpdatedAccountEvent(mAccountId, getErrorMessage()));
    }
}
