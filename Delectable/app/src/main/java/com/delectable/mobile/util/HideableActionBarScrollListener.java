package com.delectable.mobile.util;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.HideableActionBar;

import android.view.View;
import android.widget.AbsListView;

/**
 * Attaches to ListView and hides ActionBar accordingly.
 */
public class HideableActionBarScrollListener implements AbsListView.OnScrollListener {

    private static final int MIN_SCROLL = App.getInstance().getResources()
            .getDimensionPixelSize(R.dimen.min_scroll);

    private OnScrollDirectionListener mOnScrollDirectionListener;

    private int mLastTop;

    private int mLastFirstVisibleItem;

    public HideableActionBarScrollListener(final HideableActionBar hideableActionBar) {
        this(new OnScrollDirectionListener() {
            @Override
            public void onScrollUp() {
                hideableActionBar.showOrHideActionBar(false);
            }

            @Override
            public void onScrollDown() {
                hideableActionBar.showOrHideActionBar(true);
            }
        });
    }

    public HideableActionBarScrollListener(OnScrollDirectionListener listener) {
        setOnScrollDirectionListener(listener);
    }

    public void setOnScrollDirectionListener(OnScrollDirectionListener listener) {
        mOnScrollDirectionListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        onScrollListView(absListView, firstVisibleItem);
    }

    private void onScrollListView(AbsListView absListView, int firstVisibleItem) {
        if (mOnScrollDirectionListener == null) {
            return;
        }

        View view = absListView.getChildAt(0);
        int top = (view == null) ? 0 : view.getTop();

        if (firstVisibleItem == mLastFirstVisibleItem) {
            if (top > mLastTop && (top - mLastTop > MIN_SCROLL)) {
                mOnScrollDirectionListener.onScrollDown();
            } else if (top < mLastTop && (mLastTop - top > MIN_SCROLL)) {
                mOnScrollDirectionListener.onScrollUp();
            }
        } else {
            if (firstVisibleItem < mLastFirstVisibleItem) {
                mOnScrollDirectionListener.onScrollDown();
            } else {
                mOnScrollDirectionListener.onScrollUp();
            }
        }

        mLastTop = top;
        mLastFirstVisibleItem = firstVisibleItem;
    }

    public interface OnScrollDirectionListener {

        public void onScrollUp();

        public void onScrollDown();
    }

}
