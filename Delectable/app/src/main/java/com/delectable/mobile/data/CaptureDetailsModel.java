package com.delectable.mobile.data;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.CaptureDetails;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class CaptureDetailsModel {

    private static final String TAG = CaptureDetailsModel.class.getSimpleName();

    private static final String KEY_PREFIX = "capture_details_";

    @Inject
    CacheManager mCache;

    private Type mCaptureType = new TypeToken<CaptureDetails>() {
    }.getType();

    public CaptureDetails getCapture(String id) {
        return (CaptureDetails) mCache.get(KEY_PREFIX + id, CaptureDetails.class, mCaptureType);
    }

    public void saveCaptureDetails(CaptureDetails captureDetails) {
        mCache.put(KEY_PREFIX + captureDetails.getId(), captureDetails);
    }

    public void deleteCaptureDetails(String captureId) {
        mCache.unset(KEY_PREFIX + captureId);
    }
}
