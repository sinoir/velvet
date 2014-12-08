package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.AccountModel;
import com.delectable.mobile.api.endpointmodels.accounts.AccountContext;
import com.delectable.mobile.api.endpointmodels.accounts.AccountProfileResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsContextRequest;
import com.delectable.mobile.api.events.accounts.FetchedAccountProfileEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.api.net.NetworkClient;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class FetchAccountProfileJob extends BaseJob {

    private static final String TAG = FetchAccountProfileJob.class.getSimpleName();

    @Inject
    protected AccountModel mAccountModel;

    @Inject
    EventBus mEventBus;

    @Inject
    NetworkClient mNetworkClient;

    private String mAccountId;

    public FetchAccountProfileJob(String id) {
        super(new Params(Priority.UX.value()).requireNetwork());
        mAccountId = id;
    }

    @Override
    public void onRun() throws Throwable {

        String eTag = null;
        AccountProfile cachedAccount = mAccountModel.getAccount(mAccountId);
        if (cachedAccount != null) {
            eTag = cachedAccount.getETag();
        }

        String endpoint = "/accounts/context";
        AccountsContextRequest request = new AccountsContextRequest(AccountContext.PROFILE, eTag,
                mAccountId);
        AccountProfileResponse response = mNetworkClient
                .post(endpoint, request, AccountProfileResponse.class);

        if (!response.isETagMatch()) {
            AccountProfile account = response.getPayload().getAccount();
            mAccountModel.saveAccount(account);
            mEventBus.post(new FetchedAccountProfileEvent(account));
        }
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new FetchedAccountProfileEvent(mAccountId, getErrorMessage()));
    }
}
