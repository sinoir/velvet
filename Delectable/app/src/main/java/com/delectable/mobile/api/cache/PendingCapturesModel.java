package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.api.models.TransitionState;

import android.util.Log;

import java.util.HashMap;

public class PendingCapturesModel {

    private static final String TAG = PendingCapturesModel.class.getSimpleName();

    private static final String KEY_PREFIX = "pending_captures_";

    /**
     * static final Map used as a singleton for caching.
     */
    private final HashMap<String, PendingCapture> mMap
            = new HashMap<String, PendingCapture>();

    public PendingCapture getCapture(String id) {
        String key = KEY_PREFIX + id;
        return mMap.get(key);
    }

    public void saveCapture(PendingCapture capture) {
        String key = KEY_PREFIX + capture.getId();
        mMap.put(key, capture);
    }

    public void setCaptureTransitionState(String id, TransitionState state) {
        PendingCapture capture =  getCapture(id);
        if (capture == null) {
            Log.wtf(TAG, "capture to be transacted upon doesn't exist in cache");
            return;
        }

        capture.setTransitionState(state);
        if (state == TransitionState.SYNCED) {
            capture.setTransacting(false);
        } else {
            capture.setTransacting(true);
        }
    }

    public void deleteCapture(String id) {
        String key = KEY_PREFIX + id;
        mMap.remove(key);
    }

    public void clear() {
        mMap.clear();
    }
}
