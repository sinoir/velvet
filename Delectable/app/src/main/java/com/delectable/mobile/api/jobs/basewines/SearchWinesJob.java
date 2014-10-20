package com.delectable.mobile.api.jobs.basewines;

import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.events.basewines.SearchWinesEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.endpointmodels.SearchRequest;
import com.delectable.mobile.api.endpointmodels.basewines.BaseWinesSearchResponse;
import com.delectable.mobile.util.KahunaUtil;
import com.path.android.jobqueue.Params;

public class SearchWinesJob extends BaseJob {

    private static final String TAG = SearchWinesJob.class.getSimpleName();

    private String mQ;

    private int mOffset;

    private int mLimit;

    public SearchWinesJob(String q, int offset, int limit) {
        super(new Params(Priority.SYNC));
        mQ = q;
        mOffset = offset;
        mLimit = limit;

    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/base_wines/search";
        SearchRequest request = new SearchRequest(mQ, mOffset, mLimit);
        BaseWinesSearchResponse response = getNetworkClient().post(endpoint, request,
                BaseWinesSearchResponse.class);
        getEventBus().post(new SearchWinesEvent(response.getPayload()));
        KahunaUtil.trackSearch();
    }

    @Override
    protected void onCancel() {
        getEventBus().post(new SearchWinesEvent(TAG + " " + getErrorMessage(), getErrorCode()));
    }
}
