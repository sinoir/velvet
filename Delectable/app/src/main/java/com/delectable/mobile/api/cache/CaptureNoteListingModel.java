package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.cache.localmodels.CacheListing;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.Listing;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class CaptureNoteListingModel {

    private static final String TAG = CaptureNoteListingModel.class.getSimpleName();

    private static final String KEY_PREFIX = "capture_note_listing_";

    /**
     * static final Map used as a singleton for caching.
     */
    private static final HashMap<String, CacheListing<CaptureNote>> mMap
            = new HashMap<String, CacheListing<CaptureNote>>();


    public static void saveListing(String wineId, Listing<CaptureNote> listing) {
        String key = KEY_PREFIX + wineId;
        CacheListing<CaptureNote> cacheListing = new CacheListing<CaptureNote>(listing);
        mMap.put(key, cacheListing);
        // Save all captures separately
        for (CaptureNote capture : listing.getUpdates()) {
            CaptureNoteModel.saveCaptureNote(capture);
        }
    }

    public static Listing<CaptureNote> getUserCaptures(String wineId) {
        String key = KEY_PREFIX + wineId;
        return getCachedListing(key);
    }

    private static Listing<CaptureNote> getCachedListing(String key) {
        CacheListing<CaptureNote> cacheListing = mMap.get(key);
        if (cacheListing == null) {
            //nothing in cache
            return null;
        }

        //build Listing from CacheListing
        ArrayList<CaptureNote> captures = new ArrayList<CaptureNote>();
        for (String captureId : cacheListing.getItemIds()) {
            CaptureNote capture = CaptureNoteModel.getCaptureNote(captureId);
            if (capture != null) {
                captures.add(capture);
            } else {
                Log.e(TAG,
                        "Listing from cache inconsistency, capture id from cachelisting object not found in cache");
            }
        }
        Listing<CaptureNote> listing = new Listing<CaptureNote>(cacheListing, captures);

        return listing;
    }


}
