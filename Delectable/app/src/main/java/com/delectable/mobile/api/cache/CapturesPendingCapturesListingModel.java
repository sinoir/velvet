package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.cache.localmodels.CacheListing;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.PendingCapture;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class CapturesPendingCapturesListingModel {

    @Inject
    protected CaptureDetailsModel mCaptureDetailsModel;

    @Inject
    protected PendingCapturesModel mPendingCapturesModel;

    private static final String TAG = CapturesPendingCapturesListingModel.class.getSimpleName();

    private static final String TYPE_USER_CAPTURES = TAG + "users_";


    private final HashMap<String, CacheListing<BaseListingElement>> mMap
            = new HashMap<String, CacheListing<BaseListingElement>>();

    public Listing<BaseListingElement> getUserCaptures(String accountId) {
        String key = TYPE_USER_CAPTURES + accountId;
        return getCachedCaptures(key);
    }

    public void saveUserCaptures(String accountId, Listing<BaseListingElement> listing) {
        String key = TYPE_USER_CAPTURES + accountId;
        saveListing(key, listing);
    }

    public void clear() {
        mMap.clear();
    }

    private void saveListing(String key, Listing<BaseListingElement> listing) {

        CacheListing<BaseListingElement> cacheListing = new CacheListing<BaseListingElement>(
                listing);
        mMap.put(key, cacheListing);
        // Save all captures separately into PendingCaptures and CaptureDetails model
        for (BaseListingElement capture : listing.getUpdates()) {
            if (capture instanceof CaptureDetails) {
                mCaptureDetailsModel.saveCaptureDetails((CaptureDetails) capture);
                continue;
            }
            if (capture instanceof PendingCapture) {
                mPendingCapturesModel.saveCapture((PendingCapture) capture);
                continue;
            }

            Log.wtf(TAG, "capture wasn't CaptureDetail or PendingCapture");
        }
    }

    private Listing<BaseListingElement> getCachedCaptures(String key) {
        CacheListing<BaseListingElement> cacheListing = mMap.get(key);
        if (cacheListing == null) {
            //nothing in cache
            return null;
        }

        //build Listing from CacheListing
        ArrayList<BaseListingElement> captures = new ArrayList<BaseListingElement>();
        for (String captureId : cacheListing.getItemIds()) {

            //first check to see if capture exists in captureDetailsModel
            BaseListingElement capture = mCaptureDetailsModel.getCapture(captureId);
            if (capture != null) {
                captures.add(capture);
                continue;
            }

            //didn't find capture in capturedetailsmodel, see if it's in pendingcapturesmodel
            capture = mPendingCapturesModel.getCapture(captureId);
            if (capture != null) {
                captures.add(capture);
                continue;
            }

            Log.e(TAG,
                    "Listing from cache inconsistency, capture id from cachelisting object not found in cache");
        }
        Listing<BaseListingElement> listing = new Listing<BaseListingElement>(cacheListing,
                captures);

        return listing;
    }
}
