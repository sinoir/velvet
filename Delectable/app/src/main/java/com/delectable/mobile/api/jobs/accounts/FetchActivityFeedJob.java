package com.delectable.mobile.api.jobs.accounts;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.endpointmodels.ListingRequest;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.Priority;
import com.delectable.mobile.api.models.ActivityFeedItem;
import com.path.android.jobqueue.Params;

import java.lang.reflect.Type;

public class FetchActivityFeedJob extends BaseJob {

    private static final String TAG = FetchActivityFeedJob.class.getSimpleName();

    private String mRequestId;

    private String mContext;

    private String mETag;

    private String mId;

    private String mBefore;

    private String mAfter;

    private Boolean mIsPullToRefresh;

    public FetchActivityFeedJob(String requestId, String before, String after,
            Boolean isPullToRefresh) {
        super(new Params(Priority.SYNC).requireNetwork());

        //TODO optimize for etag use
        mRequestId = requestId;
        mBefore = before;
        mAfter = after;
        mIsPullToRefresh = isPullToRefresh;
    }

    @Override
    public void onRun() throws Throwable {
        String endpoint = "/accounts/activity_feed";
        ListingRequest request = new ListingRequest(mContext, mETag, null, mBefore, mAfter,
                mIsPullToRefresh);
        Type type = new TypeToken<ListingResponse<ActivityFeedItem, String>>() {
        }.getType();
        ListingResponse<ActivityFeedItem, String> response = getNetworkClient().post(
                endpoint, request, type);
        getEventBus().post(new UpdatedListingEvent<ActivityFeedItem, String>(mRequestId, null,
                response.getPayload()));


    }

    @Override
    protected void onCancel() {
        getEventBus().post(new UpdatedListingEvent(mRequestId, null, TAG + " " + getErrorMessage(),
                getErrorCode()));
    }
}
