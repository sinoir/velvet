package com.delectable.mobile.data;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class CaptureDetailsListingModel {

    private static final String TAG = CaptureDetailsListingModel.class.getSimpleName();

    private static final String KEY_PREFIX = "capture_details_listing_";

    public static final String TYPE_USER_CAPTURES = KEY_PREFIX + "users_";

    // TODO: Test /impelement Pending Captures
    public static final String TYPE_PENDING_CAPTURES = KEY_PREFIX + "pending_";

    public static final String TYPE_FOLLOWING_CAPTURES = KEY_PREFIX + "following";

    @Inject
    CacheManager mCache;

    private Type mClassType = new TypeToken<ListingResponse<CaptureDetails>>() {
    }.getType();

    //region Save CaptureListing methods
    private void saveCaptureListing(String type, String accountId,
            ListingResponse<CaptureDetails> captureListing) {
        mCache.put(type + accountId, captureListing);
    }

    public void saveUserCaptures(String accountId, ListingResponse<CaptureDetails> captureListing) {
        // TODO: Should store each capture separately in a CaptureDetailsModel
        // For example: we should recombine the data later when we fetch it, this lets us cache
        // each individual capture instead of having to make a duplicate copy
        saveCaptureListing(TYPE_USER_CAPTURES, accountId, captureListing);
    }

    public void savePendingCaptures(String accountId,
            ListingResponse<CaptureDetails> captureListing) {
        saveCaptureListing(TYPE_PENDING_CAPTURES, accountId, captureListing);
    }

    public void saveFollowerFeedCaptures(ListingResponse<CaptureDetails> captureListing) {
        saveCaptureListing(TYPE_FOLLOWING_CAPTURES, "", captureListing);
    }
    //endregion

    //region Get CaptureListing Methods
    private ListingResponse<CaptureDetails> getCaptureListing(String type, String accountId) {
        // TODO: Should retrieve each capture separately and merge it into the listingResponse Object.
        String key = type + accountId;
        ListingResponse<CaptureDetails> captures = (ListingResponse<CaptureDetails>) mCache.get(key,
                ListingResponse.class, mClassType);
        return captures;
    }

    public ListingResponse<CaptureDetails> getUserCaptures(String accountId) {
        return getCaptureListing(TYPE_USER_CAPTURES, accountId);
    }

    public ListingResponse<CaptureDetails> getPendingCaptures(String accountId) {
        return getCaptureListing(TYPE_PENDING_CAPTURES, accountId);
    }

    public ListingResponse<CaptureDetails> getFollowerFeedCaptures() {
        // Follower Feed is just a listing, no account ID linked.
        return getCaptureListing(TYPE_FOLLOWING_CAPTURES, "");
    }
    //endregion
}
