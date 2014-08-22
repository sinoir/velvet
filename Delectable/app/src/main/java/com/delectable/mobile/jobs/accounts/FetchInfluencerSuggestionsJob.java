package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.FetchInfluencerSuggestionsEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.BaseRequest;
import com.delectable.mobile.model.api.accounts.AccountsInfluencerSuggestionsResponse;
import com.path.android.jobqueue.Params;

public class FetchInfluencerSuggestionsJob extends BaseJob {

    private static final String TAG = FetchInfluencerSuggestionsJob.class.getSimpleName();

    public FetchInfluencerSuggestionsJob() {
        super(new Params(Priority.SYNC).requireNetwork().persist());
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/influencer_suggestions";
        BaseRequest request = new BaseRequest(); //empty payload, so we use baserequest
        AccountsInfluencerSuggestionsResponse response = getNetworkClient().post(endpoint, request,
                AccountsInfluencerSuggestionsResponse.class);
        getEventBus()
                .post(new FetchInfluencerSuggestionsEvent(response.getPayload().getAccounts()));
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new FetchInfluencerSuggestionsEvent(getErrorMessage()));
    }
}
