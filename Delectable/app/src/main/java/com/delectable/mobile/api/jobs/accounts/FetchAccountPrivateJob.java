package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.endpointmodels.accounts.AccountContext;
import com.delectable.mobile.api.endpointmodels.accounts.AccountPrivateResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsContextRequest;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.events.accounts.UpdatedCaptureFeedsEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.CaptureFeed;
import com.path.android.jobqueue.Params;

import java.util.List;

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
        super(new Params(Priority.UX.value()).requireNetwork());
        mAccountId = id;
    }

    @Override
    public void onRun() throws Throwable {

        String endpoint = "/accounts/context";
        AccountsContextRequest request = new AccountsContextRequest(AccountContext.PRIVATE,
                mAccountId);
        AccountPrivateResponse response = mNetworkClient
                .post(endpoint, request, AccountPrivateResponse.class);
        Account account = response.getPayload().getAccount();

        //cache to shared prefs
        UserInfo.setAccountPrivate(account);
        mEventBus.post(new UpdatedAccountEvent(account));

        //save capture feeds
        List<CaptureFeed> oldFeeds = UserInfo.getCaptureFeeds();
        if (account.getCaptureFeeds() != null && !account.getCaptureFeeds().equals(oldFeeds)) {
            UserInfo.setCaptureFeeds(account.getCaptureFeeds());
            mEventBus.post(new UpdatedCaptureFeedsEvent(account.getCaptureFeeds(), oldFeeds));
        }

    }

    @Override
    protected void onCancel() {
        mEventBus.post(new UpdatedAccountEvent(mAccountId, getErrorMessage()));
    }
}
