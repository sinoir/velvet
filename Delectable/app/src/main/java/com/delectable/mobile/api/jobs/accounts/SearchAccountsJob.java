package com.delectable.mobile.api.jobs.accounts;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.endpointmodels.SearchRequest;
import com.delectable.mobile.api.endpointmodels.SearchResponse;
import com.delectable.mobile.api.endpointmodels.accounts.SearchAccountsRequest;
import com.delectable.mobile.api.events.accounts.SearchAccountsEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

import java.lang.reflect.Type;

public class SearchAccountsJob extends BaseJob {

    private static final String TAG = SearchAccountsJob.class.getSimpleName();

    private String mQ;

    private int mOffset;

    private int mLimit;

    private boolean mContextual = false;

    private String mCaptureId;

    /**
     * @param offset The index of the first item that we want.
     * @param limit  How many items to retrieve at once.
     */
    public SearchAccountsJob(String q, int offset, int limit, boolean contextual, String captureId) {
        super(new Params(Priority.SYNC.value()));
        mQ = q;
        mOffset = offset;
        mLimit = limit;
        mContextual = contextual;
        mCaptureId = captureId;

    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/search";
        SearchAccountsRequest request = new SearchAccountsRequest(mQ, mOffset, mLimit, mContextual, mCaptureId);
        Type type = new TypeToken<SearchResponse<AccountSearch>>() {
        }.getType();
        SearchResponse<AccountSearch> response = getNetworkClient()
                .post(endpoint, request, type);
        getEventBus().post(new SearchAccountsEvent(response.getPayload()));
        KahunaUtil.trackSearch();
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new SearchAccountsEvent(TAG + " " + getErrorMessage(), getErrorCode()));
    }
}
