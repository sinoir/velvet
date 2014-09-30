package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.events.accounts.UpdatedAccountProfileEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountContextRequest;
import com.delectable.mobile.model.api.accounts.AccountPrivateResponse;
import com.delectable.mobile.model.api.accounts.AccountProfileResponse;
import com.delectable.mobile.net.NetworkClient;
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
        super(new Params(Priority.UX).requireNetwork());
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
        AccountContextRequest request = new AccountContextRequest(Account.Context.PROFILE, eTag, mAccountId);
        AccountProfileResponse response = mNetworkClient
                .post(endpoint, request, AccountProfileResponse.class);

        if (!response.isETagMatch()) {
            AccountProfile account = response.getPayload().getAccount();
            mAccountModel.saveAccount(account);
            mEventBus.post(new UpdatedAccountProfileEvent(account));
        }
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new UpdatedAccountProfileEvent(mAccountId, getErrorMessage()));
    }
}
