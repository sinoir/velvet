package com.delectable.mobile.ui.common.widget;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class TouchableSpan extends ClickableSpan {

    private int mTouchableTextColor;

    public TouchableSpan(int touchableTextColor) {
        mTouchableTextColor = touchableTextColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mTouchableTextColor);
        ds.setUnderlineText(false);
    }

}
