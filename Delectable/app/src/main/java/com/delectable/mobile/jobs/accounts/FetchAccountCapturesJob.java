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

public class FetchAccountCapturesJob extends BaseFetchListingJob<CaptureDetails> {

    private static final String TAG = FetchAccountCapturesJob.class.getSimpleName();

    @Inject
    protected CaptureListingModel mListingModel;

    @Override
    public String getEndpoint() {
        return "/accounts/captures";
    }

    @Override
    public BaseListingResponse<CaptureDetails> getCachedListing(String accountId) {
        return mListingModel.getListing(mAccountId);
    }

    @Override
    public void saveListingToCache(String accountId,
            BaseListingResponse<CaptureDetails> apiListing) {
        mListingModel.saveCurrentListing(mAccountId, apiListing);
    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<BaseListingWrapperResponse<CaptureDetails>>() {
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
            BaseListingResponse<CaptureDetails> captureListing,
            Boolean isPullToRefresh) {
        super(requestId, context.toString(), accountId, captureListing, isPullToRefresh);
    }
}
