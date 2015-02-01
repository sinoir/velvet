package com.mienaikoe.wifimesh;

import android.content.Context;
import android.graphics.Typeface;

public enum FontEnum {

    HELVETICA_NEUE("HelveticaNeue.otf"),
    HELVETICA_NEUE_MEDIUM("HelveticaNeue-Medium.otf");

    private String mFileName;

    private Typeface mTypeface;

    private FontEnum(String fileName) {
        mFileName = fileName;
    }

    public String getFileName() {
        return mFileName;
    }

    public Typeface getTypeface(Context context) {
        //lazily instantiate typeface for each fontenum object
        if (mTypeface == null) {
           mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + mFileName);
        }
        return mTypeface;
    }
}
