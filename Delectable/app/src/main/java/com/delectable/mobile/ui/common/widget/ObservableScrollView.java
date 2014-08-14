package com.delectable.mobile.ui.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * A custom ScrollView that can accept a scroll listener.
 */
public class ObservableScrollView extends ScrollView {

    private Callbacks mCallbacks;

    private boolean mIsScrollingCanceled = false;

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mIsScrollingCanceled) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mIsScrollingCanceled) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(l - oldl, t - oldt);
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        boolean result = super
                .overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                        maxOverScrollX, maxOverScrollY, isTouchEvent);
        if (mCallbacks != null) {
            mCallbacks.onScrollViewOverScrollBy(deltaY, scrollY, isTouchEvent, result);
        }
        return result;
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setScrollingCanceled(boolean isScrollingCanceled) {
        mIsScrollingCanceled = isScrollingCanceled;
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    public static interface Callbacks {

        public void onScrollChanged(int deltaX, int deltaY);

        public void onScrollViewOverScrollBy(int deltaY, int scrollY, boolean isTouchEvent,
                boolean overScrollResult);
    }
}