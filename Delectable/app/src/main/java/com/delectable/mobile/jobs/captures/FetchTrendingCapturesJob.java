package com.delectable.mobile.jobs.captures;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureListingModel;
import com.delectable.mobile.jobs.BaseFetchListingJob;
import com.delectable.mobile.api.endpointmodels.BaseListingWrapperResponse;
import com.delectable.mobile.api.endpointmodels.accounts.CapturesContext;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class FetchTrendingCapturesJob extends BaseFetchListingJob<CaptureDetails> {

    private static final String TAG = FetchTrendingCapturesJob.class.getSimpleName();

    @Inject
    protected CaptureListingModel mListingModel;

    @Override
    public String getEndpoint() {
        return "/captures/trending";
    }

    @Override
    public BaseListingResponse<CaptureDetails> getCachedListing(String accountId) {
        return mListingModel.getTrendingFeed();
    }

    @Override
    public void saveListingToCache(String accountId,
            BaseListingResponse<CaptureDetails> apiListing) {
        mListingModel.saveTrendingFeed(apiListing);
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
    //only details context seems to be working with this endpoint currently
    public FetchTrendingCapturesJob(String requestId, CapturesContext context,
            BaseListingResponse<CaptureDetails> captureListing,
            Boolean isPullToRefresh) {
        super(requestId, context.toString(), null, captureListing, isPullToRefresh);
    }
}
