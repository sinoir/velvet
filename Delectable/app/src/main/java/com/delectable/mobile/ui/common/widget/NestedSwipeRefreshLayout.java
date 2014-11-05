package com.delectable.mobile.ui.common.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class NestedSwipeRefreshLayout extends SwipeRefreshLayout {

    private ListView mListView;

    public NestedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mListView == null || mListView.getVisibility() != View.VISIBLE) {
            return false;
        }
        return mListView.getFirstVisiblePosition() > 0
                || mListView.getChildAt(0) == null
                || mListView.getChildAt(0).getTop() < 0;
    }

}
