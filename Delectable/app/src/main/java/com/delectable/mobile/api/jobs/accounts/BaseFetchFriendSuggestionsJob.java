package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.accounts.AccountMinimalListResponse;
import com.delectable.mobile.api.events.accounts.FetchFriendSuggestionsEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.path.android.jobqueue.Params;

/**
 * The endpoints for fetching influencer suggestions and fetching facebook suggestions are the same,
 * this base class contains their shared logic.
 */
public abstract class BaseFetchFriendSuggestionsJob extends BaseJob {

    private static final String TAG = BaseFetchFriendSuggestionsJob.class.getSimpleName();

    private String mId;

    /**
     * @param id The id that will be passed onto the Event upon completion of the job.
     */
    protected BaseFetchFriendSuggestionsJob(String id) {
        super(new Params(Priority.SYNC.value()).requireNetwork());
        mId = id;
    }

    public abstract String getEndpoint();

    @Override
    public void onRun() throws Throwable {
        String endpoint = getEndpoint();
        BaseRequest request = new BaseRequest(); //empty payload, so we use baserequest
        AccountMinimalListResponse response = getNetworkClient().post(endpoint, request,
                AccountMinimalListResponse.class);
        getEventBus()
                .post(new FetchFriendSuggestionsEvent(mId, response.getPayload().getAccounts()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchFriendSuggestionsEvent(mId, getErrorMessage(), getErrorCode()));
    }
}
