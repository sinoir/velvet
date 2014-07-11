package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.util.ColorsUtil;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlidingPagerTabStrip extends RelativeLayout {

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

    private int mIndicatorHeight = 4;

    private IndicatorView mIndicatorView;

    private Paint mIndicatorPaint;

    private TabColorizer mTabColorizer;

    private int mTabTextSize = 17;

    private int mTabTextAppearance;

    private SlidingPagerAdapter mPagerAdapter;

    public SlidingPagerTabStrip(Context context) {
        this(context, null);
    }

    public SlidingPagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setLayoutParams(new
                LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        addView(mTabsContainer);

        mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mIndicatorHeight, dm);

        mIndicatorView = new IndicatorView(context);
        RelativeLayout.LayoutParams indicatorLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, mIndicatorHeight);
        indicatorLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
        mIndicatorView.setLayoutParams(indicatorLayoutParams);
        addView(mIndicatorView);

        mTabLayoutParams = new
                LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mTabLayoutParams.weight = 1;

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        mTabTextAppearance = a.getResourceId(0, a.getResourceId(0, 0));
        mTabTextSize = a.getDimensionPixelSize(2, mTabTextSize);

        a.recycle();

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
        mTabColorizer = new TabColorizer();
        if (mPager.getAdapter() != null && mPager.getAdapter() instanceof SlidingPagerAdapter) {
            mPagerAdapter = (SlidingPagerAdapter) mPager.getAdapter();
            mTabCount = mPager.getAdapter().getCount();
            int tabColors[] = new int[mTabCount];
            for (int i = 0; i < mTabCount; i++) {
                if (mPagerAdapter.getPageTitle(i) != null) {
                    addTextTab(i);
                } else if (mPagerAdapter.getPageIcon(i) > 0) {
                    addImageTab(i);
                }
                tabColors[i] = getResources().getColor(mPagerAdapter.getTabIndicatorColor(i));
            }
            mTabColorizer.setIndicatorColors(tabColors);
        }
        updateTabStyles();
    }

    private void addImageTab(final int position) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(mPagerAdapter.getPageIcon(position));
        addTab(position, tab);
    }

    private void addTextTab(final int position) {
        TextView tvTab = new TextView(getContext());
        ColorStateList colorStateList = getResources()
                .getColorStateList(mPagerAdapter.getTabTitleColor(position));
        CharSequence title = mPagerAdapter.getPageTitle(position) == null ? ""
                : mPagerAdapter.getPageTitle(
                        position);
        tvTab.setText(title);
        tvTab.setGravity(Gravity.CENTER);
        tvTab.setSingleLine();
        tvTab.setTextAppearance(getContext(), mTabTextAppearance);
        tvTab.setTextColor(colorStateList);
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
            tabView.setBackgroundResource(mPagerAdapter.getBackgroundColor(i));
        }
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

    private static class TabColorizer {

        private int[] mIndicatorColors;

        public final int getIndicatorColor(int position) {
            return mIndicatorColors[position % mIndicatorColors.length];
        }

        void setIndicatorColors(int... colors) {
            mIndicatorColors = colors;
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            mIndicatorView.invalidate();
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

    class IndicatorView extends View {

        public IndicatorView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mTabCount == 0) {
                return;
            }

            final int height = getHeight();

            // Draw indicator below current tab
            View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
            float leftEdge = currentTab.getLeft();
            float rightEdge = currentTab.getRight();

            // Get Indicator Color based on Scroll Position
            int indicatorColor = mTabColorizer.getIndicatorColor(mCurrentPosition);
            int nextIndicatorColor = mTabColorizer.getIndicatorColor(mCurrentPosition + 1);

            if (indicatorColor != nextIndicatorColor) {
                indicatorColor = ColorsUtil
                        .blendColors(nextIndicatorColor, indicatorColor, mCurrentPositionOffset);
            }

            mIndicatorPaint.setColor(indicatorColor);

            // Get Indicator bounds based on scroll position
            if (mCurrentPositionOffset > 0f && mCurrentPositionOffset < mTabCount - 1) {
                View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
                final float nextTabLeftEdge = nextTab.getLeft();
                final float nextTabRightEdge = nextTab.getRight();

                leftEdge = (mCurrentPositionOffset * nextTabLeftEdge
                        + (1.0f - mCurrentPositionOffset)
                        * leftEdge);
                rightEdge = (mCurrentPositionOffset * nextTabRightEdge
                        + (1.0f - mCurrentPositionOffset)
                        * rightEdge);
            }
            canvas.drawRect(leftEdge, height - mIndicatorHeight, rightEdge, height,
                    mIndicatorPaint);
        }
    }
}
