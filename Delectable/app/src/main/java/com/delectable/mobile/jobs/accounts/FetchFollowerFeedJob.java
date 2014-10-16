package com.delectable.mobile.jobs.accounts;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureListingModel;
import com.delectable.mobile.jobs.BaseFetchListingJob;
import com.delectable.mobile.model.api.BaseListingWrapperResponse;
import com.delectable.mobile.model.api.accounts.CapturesContext;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class FetchFollowerFeedJob extends BaseFetchListingJob<CaptureDetails> {

    private static final String TAG = FetchFollowerFeedJob.class.getSimpleName();

    @Inject
    protected CaptureListingModel mListingModel;

    @Override
    public String getEndpoint() {
        return "/accounts/follower_feed";
    }

    @Override
    public BaseListingResponse<CaptureDetails> getCachedListing(String accountId) {
        return mListingModel.getFollowerFeed();
    }

    @Override
    public void saveListingToCache(String accountId,
            BaseListingResponse<CaptureDetails> apiListing) {
        mListingModel.saveFollowerFeed(apiListing);
    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<BaseListingWrapperResponse<CaptureDetails>>() {
        }.getType();
        return type;
    }

    /**
     * @param captureListing  The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public FetchFollowerFeedJob(String requestId, CapturesContext context,
            BaseListingResponse<CaptureDetails> captureListing,
            Boolean isPullToRefresh) {
        super(requestId, context.toString(), null, captureListing, isPullToRefresh);
    }
}