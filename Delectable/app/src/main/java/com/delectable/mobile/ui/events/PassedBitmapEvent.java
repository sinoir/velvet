package com.delectable.mobile.ui.events;


import android.graphics.Bitmap;

/**
 * The capture bitmap is too large to pass via a bundle between activities/fragments, so we send it using an event.
 */
public class PassedBitmapEvent {

    private Bitmap mBitmap;

    public PassedBitmapEvent(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
