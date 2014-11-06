package com.delectable.mobile.api.jobs.accounts;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.cache.CapturesPendingCapturesListingModel;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContext;
import com.delectable.mobile.api.jobs.BaseFetchListingJob;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class FetchAccountCapturesJob extends BaseFetchListingJob<BaseListingElement> {

    private static final String TAG = FetchAccountCapturesJob.class.getSimpleName();

    @Inject
    CapturesPendingCapturesListingModel mListingModel;

    @Override
    public String getEndpoint() {
        return "/accounts/captures_and_pending_captures";
    }

    @Override
    public Listing<BaseListingElement, String> getCachedListing(String dataItemId) {
        return mListingModel.getUserCaptures(dataItemId);
    }

    @Override
    public void saveListingToCache(String dataItemId, Listing<BaseListingElement, String> apiListing) {
        mListingModel.saveUserCaptures(dataItemId, apiListing);
    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<ListingResponse<BaseListingElement>>() {
        }.getType();
        return type;
    }

    /**
     * @param accountId       Account that you want to fetch captures for.
     * @param captureListing  The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public FetchAccountCapturesJob(String requestId, CapturesContext context, String accountId,
            Listing<BaseListingElement, String> captureListing,
            Boolean isPullToRefresh) {
        super(requestId, context.toString(), accountId, captureListing, isPullToRefresh);
    }
}
