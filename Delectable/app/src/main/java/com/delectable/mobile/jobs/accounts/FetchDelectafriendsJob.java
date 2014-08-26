package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.FetchedDelectafriendsEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.accounts.AccountMinimalListResponse;
import com.path.android.jobqueue.Params;

public class FetchDelectafriendsJob extends BaseJob {

    private static final String TAG = FetchDelectafriendsJob.class.getSimpleName();

    public FetchDelectafriendsJob() {
        super(new Params(Priority.SYNC).requireNetwork().persist());
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/delectafriends";
        BaseRequest request = new BaseRequest(); //empty payload, so we use baserequest
        AccountMinimalListResponse response = getNetworkClient().post(endpoint, request,
                AccountMinimalListResponse.class);
        getEventBus()
                .post(new FetchedDelectafriendsEvent(response.getPayload().getAccounts()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchedDelectafriendsEvent(getErrorMessage()));
    }
}
