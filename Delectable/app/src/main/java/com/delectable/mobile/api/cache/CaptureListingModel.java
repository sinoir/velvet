package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.cache.localmodels.CacheListing;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class CaptureListingModel {

    private static final String TAG = CaptureListingModel.class.getSimpleName();

    private static final String KEY_PREFIX = "capture_listing_";

    private static final String VERSION = "v1_";

    private static final String TYPE_USER_CAPTURES = KEY_PREFIX + "users_" + VERSION;

    private static final String TYPE_TRENDING = KEY_PREFIX + "trending_" + VERSION;

    private static final String TYPE_FOLLOW_FEED = KEY_PREFIX + "followfeed_" + VERSION;

    /**
     * static final Map used as a singleton for caching.
     */
    private static final HashMap<String, CacheListing<CaptureDetails>> mMap
            = new HashMap<String, CacheListing<CaptureDetails>>();

    @Inject
    protected CaptureDetailsModel mCaptureDetailsModel;

    /**
     * @param accountId The accountId that the listing belongs to.
     * @param listing   This ListingResponse be saved should already have been combined into the
     *                  current list, and that list have been saved as the updates array to this
     *                  object.
     */
    public void saveUserCaptures(String accountId, Listing<CaptureDetails> listing) {
        String key = TYPE_USER_CAPTURES + accountId;
        saveListing(key, listing);
    }

    public Listing<CaptureDetails> getUserCaptures(String accountId) {
        String key = TYPE_USER_CAPTURES + accountId;
        return getCachedCaptures(key);
    }

    public void saveTrendingFeed(Listing<CaptureDetails> listing) {
        saveListing(TYPE_TRENDING, listing);
    }

    public Listing<CaptureDetails> getTrendingFeed() {
        return getCachedCaptures(TYPE_TRENDING);
    }

    public void saveFollowerFeed(Listing<CaptureDetails> listing) {
        saveListing(TYPE_FOLLOW_FEED, listing);
    }

    public Listing<CaptureDetails> getFollowerFeed() {
        return getCachedCaptures(TYPE_FOLLOW_FEED);
    }

    public static void clear() {
        mMap.clear();
    }

    private void saveListing(String key, Listing<CaptureDetails> listing) {

        CacheListing<CaptureDetails> cacheListing = new CacheListing<CaptureDetails>(listing);
        mMap.put(key, cacheListing);
        // Save all captures separately
        for (CaptureDetails capture : listing.getUpdates()) {
            mCaptureDetailsModel.saveCaptureDetails(capture);
        }
    }

    private Listing<CaptureDetails> getCachedCaptures(String key) {
        CacheListing<CaptureDetails> cacheListing = mMap.get(key);
        if (cacheListing == null) {
            //nothing in cache
            return null;
        }

        //build Listing from CacheListing
        ArrayList<CaptureDetails> captures = new ArrayList<CaptureDetails>();
        for (String captureId : cacheListing.getItemIds()) {
            CaptureDetails capture = mCaptureDetailsModel.getCapture(captureId);
            if (capture != null) {
                captures.add(capture);
            } else {
                Log.e(TAG,
                        "Listing from cache inconsistency, capture id from cachelisting object not found in cache");
            }
        }
        Listing<CaptureDetails> listing = new Listing<CaptureDetails>(cacheListing, captures);

        return listing;
    }


}
