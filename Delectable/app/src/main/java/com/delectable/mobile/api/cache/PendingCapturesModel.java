package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.api.models.TransitionState;

import android.util.Log;

import java.util.HashMap;

public class PendingCapturesModel {

    private static final String TAG = PendingCapturesModel.class.getSimpleName();

    private final HashMap<String, PendingCapture> mMap
            = new HashMap<String, PendingCapture>();

    public PendingCapture getCapture(String id) {
        return mMap.get(id);
    }

    public void saveCapture(PendingCapture capture) {
        mMap.put(capture.getId(), capture);
    }

    public void setCaptureTransitionState(String id, TransitionState state) {
        PendingCapture capture = getCapture(id);
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
        mMap.remove(id);
    }

    public void clear() {
        mMap.clear();
    }
}
