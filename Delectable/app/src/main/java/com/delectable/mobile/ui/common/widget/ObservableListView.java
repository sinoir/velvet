package com.delectable.mobile.ui.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView that exposes it's vertical scroll offset and allows multiple OnScrollListeners
 */
public class ObservableListView extends ListView implements AbsListView.OnScrollListener {

    private List<OnScrollListener> mOnScrollListeners = new ArrayList<OnScrollListener>();

    private DisplayMetrics mDisplayMetrics = getContext().getResources().getDisplayMetrics();

    public ObservableListView(Context context) {
        super(context);
        init();
    }

    public ObservableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObservableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        super.setOnScrollListener(this);
    }

    @Override
    public int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        if (listener == null) {
            mOnScrollListeners.clear();
        } else {
            addOnScrollListener(listener);
        }
    }

    public void addOnScrollListener(OnScrollListener listener) {
        if (!mOnScrollListeners.contains(listener)) {
            mOnScrollListeners.add(listener);
        }
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        mOnScrollListeners.remove(listener);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        for (OnScrollListener listener : mOnScrollListeners) {
            listener.onScrollStateChanged(absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        for (OnScrollListener listener : mOnScrollListeners) {
            listener.onScroll(absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
