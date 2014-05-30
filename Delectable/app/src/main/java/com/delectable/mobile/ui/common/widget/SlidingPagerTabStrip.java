package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlidingPagerTabStrip extends FrameLayout {

    private static final int[] ATTRS = new int[]{
            android.R.attr.textAppearance,
            android.R.attr.textColor,
            android.R.attr.textSize,
    };

    private final PageListener mPageListener = new PageListener();

    private LinearLayout mTabsContainer;

    private LinearLayout.LayoutParams mTabLayoutParams;

    private ViewPager mPager;

    private int mTabCount;

    private int mCurrentPosition;

    private float mCurrentPositionOffset;

    private ViewPager.OnPageChangeListener mTabPageListener;

    // TODO: Make customizeable for xml stuff.
    private int mIndicatorColor;

    private int mIndicatorHeight = 3;

    private int mTabTextSize = 17;

    private int mSelectedTabTextColor = Color.BLACK;

    private int mUnSelectedTabTextColor;

    private int mTabTextAppearance;

    private Paint mIndicatorPaint;

    public SlidingPagerTabStrip(Context context) {
        this(context, null);
    }

    public SlidingPagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        mUnSelectedTabTextColor = Color.BLACK;
        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setLayoutParams(new
                LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        addView(mTabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        mTabLayoutParams = new
                LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mTabLayoutParams.weight = 1;

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        mTabTextAppearance = a.getResourceId(0, a.getResourceId(0, 0));
        mSelectedTabTextColor = a.getColor(1, mSelectedTabTextColor);
        mTabTextSize = a.getDimensionPixelSize(2, mTabTextSize);

        a.recycle();

        // TODO: Indicator Colors for each tab
        mIndicatorColor = getResources().getColor(R.color.d_dark_blue);
        mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mIndicatorHeight, dm);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
    }

    public void setViewPager(ViewPager viewPager) {
        final ViewPager pager = viewPager;
        pager.setOnPageChangeListener(mPageListener);
        mPager = pager;

        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        if (mPager.getAdapter() != null) {
            mTabCount = mPager.getAdapter().getCount();
            if (mPager.getAdapter() instanceof ImageTabProvider) {
                for (int i = 0; i < mTabCount; i++) {
                    addImageTab(i, ((ImageTabProvider) mPager.getAdapter()).getPageImageResId(i));
                }
            } else {
                for (int i = 0; i < mTabCount; i++) {
                    addTextTab(i, mPager.getAdapter().getPageTitle(i).toString());
                }
            }
        }
        updateTabStyles();
    }

    private void addImageTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    private void addTextTab(final int position, String title) {
        TextView tvTab = new TextView(getContext());
        int color = position == mCurrentPosition ? mSelectedTabTextColor : mUnSelectedTabTextColor;
        tvTab.setText(title);
        tvTab.setGravity(Gravity.CENTER);
        tvTab.setSingleLine();
        tvTab.setTextAppearance(getContext(), mTabTextAppearance);
        tvTab.setTextColor(color);
        tvTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabTextSize);

        // TODO: Set right typeface/font
        addTab(position, tvTab);
    }

    private void addTab(final int position, View tab) {
        tab.setSelected(position == mCurrentPosition);
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(position);
            }
        });
        mTabsContainer.addView(tab, position, mTabLayoutParams);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
            tabView.setBackgroundResource(R.drawable.tab_background);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTabCount == 0) {
            return;
        }

        final int height = getHeight();
        // Draw Indicator
        mIndicatorPaint.setColor(mIndicatorColor);

        // Draw indicator below current tab
        View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
        float leftEdge = currentTab.getLeft();
        float rightEdge = currentTab.getRight();

        if (mCurrentPositionOffset > 0f && mCurrentPositionOffset < mTabCount - 1) {
            View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
            final float nextTabLeftEdge = nextTab.getLeft();
            final float nextTabRightEdge = nextTab.getRight();
            leftEdge = (mCurrentPositionOffset * nextTabLeftEdge + (1f - mCurrentPositionOffset)
                    * leftEdge);
            rightEdge = (mCurrentPositionOffset * nextTabRightEdge + (1f - mCurrentPositionOffset)
                    * rightEdge);
        }
        canvas.drawRect(leftEdge, height - mIndicatorHeight, rightEdge, height, mIndicatorPaint);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mTabPageListener = listener;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPosition = savedState.mSavedPosition;
        requestLayout();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState saveState = new SavedState(superState);
        saveState.mSavedPosition = mCurrentPosition;
        return saveState;
    }

    protected void updateTabSelectionState(int currentPosition, int nextPosition) {
        View curTab = mTabsContainer.getChildAt(currentPosition);
        View nextTab = mTabsContainer.getChildAt(nextPosition);

        curTab.setSelected(false);
        nextTab.setSelected(true);
    }

    public interface ImageTabProvider {

        public int getPageImageResId(int position);
    }

    private static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SlidingPagerTabStrip.SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        int mSavedPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel inState) {
            super(inState);
            mSavedPosition = inState.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mSavedPosition);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            invalidate();
            if (mTabPageListener != null) {
                mTabPageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mTabPageListener != null) {
                mTabPageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            int lastPosition = (position == mCurrentPosition) && mCurrentPosition + 1 < mTabCount ?
                    mCurrentPosition + 1
                    : mCurrentPosition;
            int nextPosition = position;
            updateTabSelectionState(lastPosition, nextPosition);
            if (mTabPageListener != null) {
                mTabPageListener.onPageSelected(position);
            }
        }
    }
}
