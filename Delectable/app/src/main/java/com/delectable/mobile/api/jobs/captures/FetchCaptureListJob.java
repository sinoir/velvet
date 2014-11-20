package com.delectable.mobile.api.jobs.captures;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.cache.CaptureListingModel;
import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.endpointmodels.captures.CapturesListRequest;
import com.delectable.mobile.api.jobs.BaseFetchListingJob;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class FetchCaptureListJob extends BaseFetchListingJob<CaptureDetails, String> {

    private static final String TAG = FetchCaptureListJob.class.getSimpleName();

    @Inject
    protected CaptureListingModel mCaptureListingModel;

    private String mListKey;

    @Override
    public String getEndpoint() {
        return "/captures/list";
    }

    @Override
    public Listing<CaptureDetails, String> getCachedListing(String listKey) {
        return mCaptureListingModel.getCaptureList(listKey);
    }

    @Override
    public void saveListingToCache(String listKey, Listing<CaptureDetails, String> apiListing) {
        mCaptureListingModel.saveCaptureList(listKey, apiListing);

    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<ListingResponse<CaptureDetails, String>>() {
        }.getType();
        return type;
    }

    @Override
    protected BaseRequest getRequestObject() {
        return new CapturesListRequest(mListKey, mBefore, mAfter);
    }

    /**
     * @param listing         The previous listing if paginating. Pass in {@code null} if making a
     *                        fresh request.
     * @param isPullToRefresh true if user invoked this call via a pull to refresh.
     */
    public FetchCaptureListJob(String requestId, String listKey,
            Listing<CaptureDetails, String> listing, Boolean isPullToRefresh) {
        super(requestId, listKey);
        mListKey = listKey;
        if (listing != null) {
            mBefore = listing.getBoundariesToBefore();
            mAfter = listing.getBoundariesToAfter();
            mETag = listing.getETag();
        }
        mIsPullToRefresh = isPullToRefresh;
    }

}
