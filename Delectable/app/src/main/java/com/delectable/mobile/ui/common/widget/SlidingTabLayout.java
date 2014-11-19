package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

/**
 * To be used with ViewPager to provide a tab indicator component which give constant feedback as to
 * the user's scroll progress. <p> To use the component, simply add it to your view hierarchy. Then
 * in your {@link android.app.Activity} or {@link android.support.v4.app.Fragment} call {@link
 * #setViewPager(android.support.v4.view.ViewPager)} providing it the ViewPager this layout is being
 * used for. The ViewPager needs to use a {@link com.delectable.mobile.ui.common.widget.SlidingTabAdapter}.
 */
public class SlidingTabLayout extends HorizontalScrollView {

    /**
     * Allows complete control over the colors drawn in the tab layout.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

        /**
         * @return return the color of the divider drawn to the right of {@code position}.
         */
        int getDividerColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;

    private int mTitleOffset;

    private int mTabViewLayoutId = R.layout.sliding_tab_view;

    private int mTabViewTextViewId = R.id.sliding_tab_textview;

    private int mTabViewUpdateIndicatorId = R.id.sliding_tab_update_indicator;

    private ViewPager mViewPager;

    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final SlidingTabStrip mTabStrip;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same
     * color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Set the {@link android.support.v4.view.ViewPager.OnPageChangeListener}. When using {@link
     * com.delectable.mobile.ui.common.widget.SlidingTabLayout} you are required to set any {@link
     * android.support.v4.view.ViewPager.OnPageChangeListener} through this method. This is so that
     * the layout can update it's scroll position correctly.
     *
     * @see android.support.v4.view.ViewPager#setOnPageChangeListener(android.support.v4.view.ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        if (!(viewPager.getAdapter() instanceof SlidingTabAdapter)) {
            throw new IllegalArgumentException("ViewPager needs to use a SlidingTabAdapter");
        }

        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    private void populateTabStrip() {
        final SlidingTabAdapter adapter = (SlidingTabAdapter) mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;
            View tabUpdateIndicator = null;

            tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);
            tabView.setOnClickListener(tabClickListener);
            tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            tabTitleView.setText(adapter.getPageTitle(i));
            tabUpdateIndicator = tabView.findViewById(mTabViewUpdateIndicatorId);
            tabUpdateIndicator.setVisibility(adapter.hasUpdates(i) ? View.VISIBLE : View.GONE);

            mTabStrip.addView(tabView);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        private int mScrollState;

        private int mLastPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            if (position != mLastPosition) {
                // selected current tab text
                TextView titleTextView = (TextView) selectedTitle.findViewById(mTabViewTextViewId);
                if (titleTextView != null) {
                    titleTextView.setSelected(true);
                }

                // unselect last tab text
                if (mLastPosition >= 0) {
                    View lastSelectedTitle = mTabStrip.getChildAt(mLastPosition);
                    TextView lastTitleTextView = (TextView) lastSelectedTitle
                            .findViewById(mTabViewTextViewId);
                    if (lastTitleTextView != null) {
                        lastTitleTextView.setSelected(false);
                    }
                }

                // remove feed update indicator
                final View tabUpdateIndicator = selectedTitle
                        .findViewById(mTabViewUpdateIndicatorId);
                if (tabUpdateIndicator != null
                        && tabUpdateIndicator.getVisibility() == View.VISIBLE) {
                    tabUpdateIndicator.animate()
                            .alpha(0)
                            .scaleX(2)
                            .scaleY(2)
                            .setDuration(300)
                            .setStartDelay(500)
                            .setInterpolator(new AccelerateInterpolator())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    tabUpdateIndicator.setVisibility(View.GONE);
                                }
                            })
                            .start();
                }

                mLastPosition = position;
            }

            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

}
