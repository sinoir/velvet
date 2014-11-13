package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.CaptureDetails;

import java.util.HashMap;

public class CaptureDetailsModel {

    private static final String TAG = CaptureDetailsModel.class.getSimpleName();

    private final HashMap<String, CaptureDetails> mMap = new HashMap<String, CaptureDetails>();

    public CaptureDetails getCapture(String id) {
        return mMap.get(id);
    }

    public void saveCaptureDetails(CaptureDetails captureDetails) {
        mMap.put(captureDetails.getId(), captureDetails);
    }

    public void deleteCaptureDetails(String captureId) {
        mMap.remove(captureId);
    }

    public void clear() {
        mMap.clear();
    }
}
