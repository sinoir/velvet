package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.ui.common.drawable.RatingsBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class RatingsBarView extends View {

    private RatingsBar mRatingsBar;

    public RatingsBarView(Context context) {
        this(context, null);
    }

    public RatingsBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingsBarView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRatingsBar = new RatingsBar(getHeight());
        setBackgroundDrawable(mRatingsBar);

        setupFakeData();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRatingsBar.setBarHeight(h);
    }

    public void setPercent(float percent) {
        mRatingsBar.setPercent(percent);
        invalidate();
    }

    /**
     * For previewing in UI editor
     */
    private void setupFakeData() {
        if (isInEditMode()) {
            setPercent(1.28f);
        }
    }
}
