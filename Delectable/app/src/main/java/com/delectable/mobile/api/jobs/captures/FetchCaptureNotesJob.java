package com.delectable.mobile.api.jobs.captures;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.cache.CaptureNoteListingModel;
import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.endpointmodels.captures.CapturesNotesRequest;
import com.delectable.mobile.api.jobs.BaseFetchListingJob;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.Listing;

import java.lang.reflect.Type;

public class FetchCaptureNotesJob extends BaseFetchListingJob<CaptureNote> {

    private static final String TAG = FetchCaptureNotesJob.class.getSimpleName();

    private String mBaseWineId;

    private String mWineProfileId;

    private String mIncludeCaptureNote; //not being used


    @Override
    public String getEndpoint() {
        return "/captures/notes";
    }

    @Override
    public Listing<CaptureNote> getCachedListing(String id) {
        return CaptureNoteListingModel.getUserCaptures(id);
    }

    @Override
    public void saveListingToCache(String id, Listing<CaptureNote> apiListing) {
        CaptureNoteListingModel.saveListing(id, apiListing);
    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<ListingResponse<CaptureNote>>() {
        }.getType();
        return type;
    }

    @Override
    protected BaseRequest getRequestObject() {
        return new CapturesNotesRequest(mETag, mBaseWineId, mWineProfileId,
                mBefore, mAfter, mIncludeCaptureNote, mIsPullToRefresh);
    }

    /**
     * @param listing         The previous listing if paginating. Pass in {@code null} if making a
     *                        fresh request.
     * @param isPullToRefresh true if user invoked this call via a pull to refresh.
     */
    public FetchCaptureNotesJob(String requestId, String baseWineId, String wineProfileId,
            Listing<CaptureNote> listing, String includeCaptureNote, Boolean isPullToRefresh) {
        //provide either wineProfileId or baseWineId as the dataItemId
        super(requestId, baseWineId == null ? wineProfileId : baseWineId);
        mBaseWineId = baseWineId;
        mWineProfileId = wineProfileId;
        if (listing != null) {
            mBefore = listing.getBoundariesToBefore();
            mAfter = listing.getBoundariesToAfter();
            mETag = listing.getETag();
        }
        mIncludeCaptureNote = includeCaptureNote;
        mIsPullToRefresh = isPullToRefresh;

    }


}
