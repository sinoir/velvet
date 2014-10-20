package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.events.accounts.FetchedActivityFeedEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsActivityFeedRequest;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsActivityFeedResponse;
import com.path.android.jobqueue.Params;

public class FetchActivityFeedJob extends BaseJob {

    private static final String TAG = FetchActivityFeedJob.class.getSimpleName();

    private String mBefore;

    private String mAfter;

    public FetchActivityFeedJob(String before, String after) {
        super(new Params(Priority.SYNC).requireNetwork().persist());

        mBefore = before;
        mAfter = after;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/activity_feed";
        AccountsActivityFeedRequest request = new AccountsActivityFeedRequest(mBefore, mAfter);
        AccountsActivityFeedResponse response = getNetworkClient()
                .post(endpoint, request, AccountsActivityFeedResponse.class);
        getEventBus().post(new FetchedActivityFeedEvent(response.getPayload()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchedActivityFeedEvent(TAG + " " + getErrorMessage()));
    }
}
