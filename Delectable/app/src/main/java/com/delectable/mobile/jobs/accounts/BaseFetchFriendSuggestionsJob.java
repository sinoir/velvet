package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.FetchFriendSuggestionsEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.accounts.AccountsFriendSuggestionsResponse;
import com.path.android.jobqueue.Params;

/**
 * The endpoints for fetching influencer suggestions and fetching facebook suggestions are
 * the same, this base class contains their shared logic.
 */
public abstract class BaseFetchFriendSuggestionsJob extends BaseJob {

    private static final String TAG = BaseFetchFriendSuggestionsJob.class.getSimpleName();

    private String mId;

    /**
     *
     * @param id The id that will be passed onto the Event upon completion of the job.
     */
    protected BaseFetchFriendSuggestionsJob(String id) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mId = id;
    }

    public abstract String getEndpoint();

    @Override
    public void onRun() throws Throwable {
        String endpoint = getEndpoint();
        BaseRequest request = new BaseRequest(); //empty payload, so we use baserequest
        AccountsFriendSuggestionsResponse response = getNetworkClient().post(endpoint, request,
                AccountsFriendSuggestionsResponse.class);
        getEventBus()
                .post(new FetchFriendSuggestionsEvent(mId, response.getPayload().getAccounts()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchFriendSuggestionsEvent(mId, getErrorMessage()));
    }
}
