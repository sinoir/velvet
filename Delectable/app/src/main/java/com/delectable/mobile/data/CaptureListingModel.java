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

    private static final String VERSION = "v1_";

    private static final String TYPE_USER_CAPTURES = KEY_PREFIX + "users_" + VERSION;

    private static final String TYPE_TRENDING = KEY_PREFIX + "trending_" + VERSION;

    private static final String TYPE_FOLLOW_FEED = KEY_PREFIX + "followfeed_" + VERSION;




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
        String key = TYPE_USER_CAPTURES + accountId;
        saveListing(key, listing);
    }

    public BaseListingResponse<CaptureDetails> getUserCaptures(String accountId) {
        String key = TYPE_USER_CAPTURES + accountId;
        return getCachedCaptures(key);
    }

    public void saveTrendingFeed(BaseListingResponse<CaptureDetails> listing) {
        //uncomment if not saving capturedetails individually
        //mCache.put(TYPE_TRENDING, listing);
        saveListing(TYPE_TRENDING, listing);
    }

    public BaseListingResponse<CaptureDetails> getTrendingFeed() {
        //uncomment if not saving capturedetails individually
        //return getListing(TYPE_TRENDING);
        return getCachedCaptures(TYPE_TRENDING);
    }

    public void saveFollowerFeed(BaseListingResponse<CaptureDetails> listing) {
        saveListing(TYPE_FOLLOW_FEED, listing);
    }

    public BaseListingResponse<CaptureDetails> getFollowerFeed() {
        return getCachedCaptures(TYPE_FOLLOW_FEED);
    }

    private BaseListingResponse<CaptureDetails> getListing(String key) {
        Type classType = new TypeToken<BaseListingResponse<CaptureDetails>>() {
        }.getType();
        BaseListingResponse<CaptureDetails> cachelisting
                = (BaseListingResponse<CaptureDetails>) mCache
                .get(key, null, classType);
        return cachelisting;
    }

    private void saveListing(String key, BaseListingResponse<CaptureDetails> listing) {

        CacheListing<CaptureDetails> cacheListing = new CacheListing<CaptureDetails>(listing);
        mCache.put(key, cacheListing);
        // Save all captures separately
        for (CaptureDetails capture : listing.getUpdates()) {
            mCaptureDetailsModel.saveCaptureDetails(capture);
        }
    }

    private BaseListingResponse<CaptureDetails> getCachedCaptures(String key) {
        Type classType = new TypeToken<CacheListing<CaptureDetails>>() {
        }.getType();
        CacheListing<CaptureDetails> cachelisting = (CacheListing<CaptureDetails>) mCache.get(key, null, classType);
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
        BaseListingResponse<CaptureDetails> listing = new BaseListingResponse<CaptureDetails>(
                cachelisting, captures);

        return listing;
    }


}
