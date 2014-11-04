package com.delectable.mobile.api.jobs.accounts;

import com.delectable.mobile.api.cache.CaptureListingModel;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.jobs.BaseFetchListingJob;
import com.delectable.mobile.api.models.Listing;
import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContext;

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
    public Listing<CaptureDetails> getCachedListing(String accountId) {
        return mListingModel.getFollowerFeed();
    }

    @Override
    public void saveListingToCache(String accountId,
            Listing<CaptureDetails> apiListing) {
        mListingModel.saveFollowerFeed(apiListing);
    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<ListingResponse<CaptureDetails>>() {
        }.getType();
        return type;
    }

    /**
     * @param captureListing  The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public FetchFollowerFeedJob(String requestId, CapturesContext context,
            Listing<CaptureDetails> captureListing,
            Boolean isPullToRefresh) {
        super(requestId, context.toString(), null, captureListing, isPullToRefresh);
    }
}
