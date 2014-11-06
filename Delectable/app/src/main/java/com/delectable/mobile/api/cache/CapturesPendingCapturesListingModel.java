package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.cache.localmodels.CacheListing;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.PendingCapture;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class CapturesPendingCapturesListingModel {

    protected CaptureDetailsModel mCaptureDetailsModel;

    protected PendingCapturesModel mPendingCapturesModel;

    private static final String TAG = CapturesPendingCapturesListingModel.class.getSimpleName();

    private final HashMap<String, CacheListing<BaseListingElement>> mMap
            = new HashMap<String, CacheListing<BaseListingElement>>();

    public CapturesPendingCapturesListingModel(PendingCapturesModel pendingCapturesModel,
            CaptureDetailsModel capturesModel) {
        mPendingCapturesModel = pendingCapturesModel;
        mCaptureDetailsModel = capturesModel;
    }

    public Listing<BaseListingElement, String> getUserCaptures(String accountId) {
        return getCachedCaptures(accountId);
    }

    public void saveUserCaptures(String accountId, Listing<BaseListingElement, String> listing) {
        saveListing(accountId, listing);
    }

    public void discardCaptureFromList(String accountId, String captureId) {

        //first remove string id from cacheListing
        CacheListing<BaseListingElement> cacheListing = mMap.get(accountId);
        if (cacheListing == null) {
            //nothing in cache
            return;
        }
        cacheListing.removeItemId(captureId);

        //then remove object from model
        mPendingCapturesModel.deleteCapture(captureId);

        //no need to delete item from CaptureDetailsModel bc
    }

    public void clear() {
        mMap.clear();
    }

    private void saveListing(String key, Listing<BaseListingElement, String> listing) {

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

    private Listing<BaseListingElement, String> getCachedCaptures(String key) {
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
        Listing<BaseListingElement, String> listing = new Listing<BaseListingElement, String>(cacheListing,
                captures);

        return listing;
    }
}
