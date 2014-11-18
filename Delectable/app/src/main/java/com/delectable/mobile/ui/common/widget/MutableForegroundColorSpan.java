package com.delectable.mobile.ui.common.widget;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Property;

public class MutableForegroundColorSpan extends ForegroundColorSpan {

    private int mAlpha = 255;

    private int mForegroundColor;

    public MutableForegroundColorSpan(int alpha, int color) {
        super(color);
        mAlpha = alpha;
        mForegroundColor = color;
    }

    public MutableForegroundColorSpan(Parcel src) {
        super(src);
        mForegroundColor = src.readInt();
        mAlpha = src.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mForegroundColor);
        dest.writeFloat(mAlpha);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(getForegroundColor());
    }

    /**
     * @param alpha from 0 to 255
     */
    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    public void setForegroundColor(int foregroundColor) {
        mForegroundColor = foregroundColor;
    }

    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public int getForegroundColor() {
        return Color.argb(mAlpha, Color.red(mForegroundColor), Color.green(mForegroundColor),
                Color.blue(mForegroundColor));
    }

    public static final Property<MutableForegroundColorSpan, Integer> ALPHA_PROPERTY =
            new Property<MutableForegroundColorSpan, Integer>(Integer.class,
                    "MUTABLE_FOREGROUND_COLOR_SPAN_ALPHA_PROPERTY") {

                @Override
                public void set(MutableForegroundColorSpan span, Integer value) {
                    span.setAlpha(value);
                }

                @Override
                public Integer get(MutableForegroundColorSpan span) {
                    return span.getAlpha();
                }
            };

    public static final Property<MutableForegroundColorSpan, Integer> COLOR_PROPERTY =
            new Property<MutableForegroundColorSpan, Integer>(Integer.class,
                    "MUTABLE_FOREGROUND_COLOR_SPAN_COLOR_PROPERTY") {

                @Override
                public void set(MutableForegroundColorSpan span, Integer value) {
                    span.setForegroundColor(value);
                }

                @Override
                public Integer get(MutableForegroundColorSpan span) {
                    return span.getForegroundColor();
                }
            };
}
