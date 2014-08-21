package com.delectable.mobile.data;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.local.ListingObject;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

public class CaptureDetailsListingModel {

    private static final String TAG = CaptureDetailsListingModel.class.getSimpleName();

    private static final String KEY_PREFIX = "capture_details_listing_";

    public static final String TYPE_USER_CAPTURES = KEY_PREFIX + "users_";

    // TODO: Test /impelement Pending Captures
    public static final String TYPE_PENDING_CAPTURES = KEY_PREFIX + "pending_";

    public static final String TYPE_FOLLOWING_CAPTURES = KEY_PREFIX + "following";

    @Inject
    CaptureDetailsModel mCaptureDetailsModel;

    @Inject
    CacheManager mCache;

    private Type mClassType = new TypeToken<ListingObject>() {
    }.getType();

    //region Save CaptureListing methods
    private void saveCaptureListing(String type, String accountId,
            ListingResponse<CaptureDetails> captureListing) {
        ListingObject listingObject = new ListingObject(captureListing);
        mCache.put(type + accountId, listingObject);
        // Save all captures separately
        for (CaptureDetails capture : captureListing.getSortedCombinedData()) {
            mCaptureDetailsModel.saveCaptureDetails(capture);
        }
    }

    public void saveUserCaptures(String accountId, ListingResponse<CaptureDetails> captureListing) {
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
        String key = type + accountId;
        ListingObject listingObject = (ListingObject) mCache.get(key, ListingObject.class,
                mClassType);
        if (listingObject == null) {
            return null;
        }
        ListingResponse<CaptureDetails> captures = buildFromListingObject(listingObject);
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

    //Helper builder for converting ListingObject to ListingResponse
    public ListingResponse<CaptureDetails> buildFromListingObject(ListingObject object) {
        ListingResponse<CaptureDetails> captureListing = new ListingResponse<CaptureDetails>();
        captureListing.setBoundaries(object.getBoundaries());
        captureListing.setETag(object.getETag());
        captureListing.setMore(object.getMore());
        ArrayList<CaptureDetails> captures = new ArrayList<CaptureDetails>();
        for (String captureId : object.getObjectIds()) {
            CaptureDetails capture = mCaptureDetailsModel.getCapture(captureId);
            if (capture != null) {
                captures.add(capture);
            }
        }
        captureListing.setUpdates(captures);
        captureListing.updateCombinedData();

        return captureListing;
    }
}
