package com.delectable.mobile.data;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.model.local.CacheListing;
import com.iainconnor.objectcache.CacheManager;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

public class CaptureListingModel {

    private static final String TAG = CaptureListingModel.class.getSimpleName();

    private static final String KEY_PREFIX = "capture_listing_";

    private static final String TYPE_USER_CAPTURES = KEY_PREFIX + "users_";

    @Inject
    protected CaptureDetailsModel mCaptureDetailsModel;

    @Inject
    protected CacheManager mCache;

    /**
     * @param accountId The accountId that the listing belongs to.
     * @param listing   This ListingResponse be saved should already have been combined into the
     *                  current list, and that list have been saved as the updates array to this
     *                  object.
     */
    public void saveUserCaptures(String accountId, BaseListingResponse<CaptureDetails> listing) {

        CacheListing cacheListing = new CacheListing(listing);
        mCache.put(TYPE_USER_CAPTURES + accountId, cacheListing);
        // Save all captures separately
        for (CaptureDetails capture : listing.getUpdates()) {
            mCaptureDetailsModel.saveCaptureDetails(capture);
        }
    }

    public BaseListingResponse<CaptureDetails> getUserCaptures(String accountId) {
        String key = TYPE_USER_CAPTURES + accountId;

        Type classType = new TypeToken<CacheListing>() {
        }.getType();
        CacheListing cachelisting = (CacheListing) mCache.get(key, CacheListing.class, classType);
        if (cachelisting == null) {
            //nothing in cache
            return null;
        }

        //build ListingResponse from CacheListing
        ArrayList<CaptureDetails> captures = new ArrayList<CaptureDetails>();
        for (String captureId : cachelisting.getItemIds()) {
            CaptureDetails capture = mCaptureDetailsModel.getCapture(captureId);
            if (capture != null) {
                captures.add(capture);
            } else {
                Log.e(TAG,
                        "Listing from cache inconsistency, capture id from cachelisting object not found in cache");
            }
        }
        BaseListingResponse<CaptureDetails> listing = new BaseListingResponse<CaptureDetails>(cachelisting, captures);

        return listing;
    }


}
