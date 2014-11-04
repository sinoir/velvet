package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.CaptureDetails;

import java.util.HashMap;

public class CaptureDetailsModel {

    private static final String TAG = CaptureDetailsModel.class.getSimpleName();

    private static final String KEY_PREFIX = "capture_details_";

    /**
     * static final Map used as a singleton for caching.
     */
    private static final HashMap<String, CaptureDetails> mMap = new HashMap<String, CaptureDetails>();

    public CaptureDetails getCapture(String id) {
        return mMap.get(KEY_PREFIX + id);
    }

    public void saveCaptureDetails(CaptureDetails captureDetails) {
        String key = KEY_PREFIX + captureDetails.getId();
        mMap.put(key, captureDetails);
    }

    public void deleteCaptureDetails(String captureId) {
        mMap.remove(KEY_PREFIX + captureId);
    }

    public static void clear() {
        mMap.clear();
    }
}
