package com.delectable.mobile.api.jobs.hashtags;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.endpointmodels.SearchResponse;
import com.delectable.mobile.api.endpointmodels.hashtags.SearchHashtagsRequest;
import com.delectable.mobile.api.events.hashtags.SearchHashtagsEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.HashtagResult;
import com.delectable.mobile.api.models.SearchHit;
import com.path.android.jobqueue.Params;

import android.util.Log;

import java.lang.reflect.Type;

/**
 * We don't take a context parameter here, so the API will default to context minimal. If in the
 * future we do take a context for this search, remember that BaseWine's profile context doesn't
 * return a populated WineProfile ArrayList in this call
 */
public class SearchHashtagsJob extends BaseJob {

    private static final String TAG = SearchHashtagsJob.class.getSimpleName();

    private String mQ;

    private int mOffset;

    private int mLimit;

    private boolean mCaptureCounts;

    private String mWineProfileId;

    private String mBaseWineId;

    private String mCaptureId;

    private String mPendingCaptureId;

    public SearchHashtagsJob(String q, int offset, int limit) {
        this(q, offset, limit, false, null, null, null, null);
    }

    public SearchHashtagsJob(String q, int offset, int limit, boolean capture_counts,
            String wine_profile_id, String base_wine_id, String capture_id,
            String pending_capture_id) {
        super(new Params(Priority.SYNC.value()));
        mQ = q;
        mOffset = offset;
        mLimit = limit;
        mCaptureCounts = capture_counts;
        mWineProfileId = wine_profile_id;
        mBaseWineId = base_wine_id;
        mCaptureId = capture_id;
        mPendingCaptureId = pending_capture_id;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/hashtags/search";
        SearchHashtagsRequest request = new SearchHashtagsRequest(mQ, mOffset, mLimit);
        Type type = new TypeToken<SearchResponse<HashtagResult>>() {
        }.getType();
        SearchResponse<HashtagResult> response = mNetworkClient.post(endpoint, request, type);
        for (SearchHit<HashtagResult> result : response.getPayload().getHits()) {
            Log.d(TAG, result.getObject().toString());
        }
        getEventBus().post(new SearchHashtagsEvent(response.getPayload()));

    }

    @Override
    protected void onCancel() {
        getEventBus().post(new SearchHashtagsEvent(TAG + " " + getErrorMessage(), getErrorCode()));
    }
}
