package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.events.accounts.SearchAccountsEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.SearchRequest;
import com.delectable.mobile.model.api.accounts.AccountsSearchResponse;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

public class SearchAccountsJob extends BaseJob {

    private static final String TAG = SearchAccountsJob.class.getSimpleName();

    private String mQ;

    private int mOffset;

    private int mLimit;

    /**
     * @param offset The index of the first item that we want.
     * @param limit  How many items to retrieve at once.
     */
    public SearchAccountsJob(String q, int offset, int limit) {
        super(new Params(Priority.SYNC));
        mQ = q;
        mOffset = offset;
        mLimit = limit;

    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/search";
        SearchRequest request = new SearchRequest(mQ, mOffset, mLimit);
        AccountsSearchResponse response = getNetworkClient().post(endpoint, request,
                AccountsSearchResponse.class);
        getEventBus().post(new SearchAccountsEvent(response.getPayload()));
        KahunaUtil.trackSearch();
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new SearchAccountsEvent(TAG + " " + getErrorMessage(), getErrorCode()));
    }
}
