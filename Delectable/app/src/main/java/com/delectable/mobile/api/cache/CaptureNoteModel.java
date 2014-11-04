package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.CaptureNote;

import java.util.HashMap;

public class CaptureNoteModel {

    private static final String TAG = CaptureNoteModel.class.getSimpleName();

    private static final HashMap<String, CaptureNote> mMap = new HashMap<String, CaptureNote>();

    public static CaptureNote getCaptureNote(String id) {
        return mMap.get(id);
    }

    public static void saveCaptureNote(CaptureNote baseWine) {
        mMap.put(baseWine.getId(), baseWine);
    }

    public static void clear() {
        mMap.clear();
    }
}
