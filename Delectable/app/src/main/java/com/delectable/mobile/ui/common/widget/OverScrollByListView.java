package com.delectable.mobile.ui.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class OverScrollByListView extends ListView {

    private ScrollByCallback mCallback;

    private boolean mIsScrollingCanceled;

    public OverScrollByListView(Context context) {
        super(context);
    }

    public OverScrollByListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverScrollByListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCallback(ScrollByCallback callback) {
        mCallback = callback;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        boolean isClamped = super
                .overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                        maxOverScrollX, maxOverScrollY, isTouchEvent);
        if (this.mCallback != null) {
            this.mCallback.overScrolledY(deltaY, scrollY, isTouchEvent);

        }
        return isClamped;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsScrollingCanceled && ev.getAction() == MotionEvent.ACTION_MOVE) {
            return false;
        }
        // UnCancel scrolling , such as on cancel, up, or down actions, etc.
        if (mIsScrollingCanceled) {
            mIsScrollingCanceled = false;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * Helper to cancel scrolling mid scrolling, such as when we overscroll. Scrolling is re-enabled
     * automatically when user removes their finger
     */
    public void cancelScrolling() {
        mIsScrollingCanceled = true;
    }

    public static abstract interface ScrollByCallback {

        public abstract void overScrolledY(int deltaY, int scrollY, boolean isTouchEvent);
    }

}