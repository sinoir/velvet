package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.accounts.AccountContextRequest;
import com.delectable.mobile.api.endpointmodels.accounts.AccountPrivateResponse;
import com.path.android.jobqueue.Params;

public class FetchAccountPrivateJob extends BaseJob {

    private static final String TAG = FetchAccountPrivateJob.class.getSimpleName();

    private String mAccountId;

    /**
     * Fetch own private account.
     */
    public FetchAccountPrivateJob() {
        this(null); //passing in no id fetches own account
    }

    /**
     * Explicitly search for Account private with id. Shouldn't be possible to search for another
     * user's Account private data.
     */
    public FetchAccountPrivateJob(String id) {
        super(new Params(Priority.UX).requireNetwork());
        mAccountId = id;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/accounts/context";
        AccountContextRequest request = new AccountContextRequest(Account.Context.PRIVATE,
                mAccountId);
        AccountPrivateResponse response = mNetworkClient
                .post(endpoint, request, AccountPrivateResponse.class);
        Account account = response.getPayload().getAccount();

        //cache to shared prefs
        UserInfo.setAccountPrivate(account);
        mEventBus.post(new UpdatedAccountEvent(account));
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new UpdatedAccountEvent(mAccountId, getErrorMessage()));
    }
}
