package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.drawable.RatingsBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class RatingSeekBar extends SeekBar implements SeekBar.OnSeekBarChangeListener {

    private RatingsBar mRatingsBar;

    private OnRatingsChangeListener mRatingChangeListener;

    // Can toggle this to use bar colors
    private boolean mShowColors = false;

    public RatingSeekBar(Context context) {
        this(context, null);
    }

    public RatingSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int barHeight = context.getResources().getDimensionPixelSize(R.dimen.rating_bar_seek_height);
        mRatingsBar = new RatingsBar(barHeight);
        setProgressDrawable(mRatingsBar);
        Drawable thumb = getResources().getDrawable(R.drawable.btn_rating_bar_slider_normal);
        setThumb(thumb);
        setThumbOffset(0);

        setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser || mShowColors) {
            mRatingsBar.setPercent(progress / 40.0f);
        }
        if (mRatingChangeListener != null) {
            mRatingChangeListener.onRatingsChanged(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void setOnRatingChangeListener(OnRatingsChangeListener ratingChangeListener) {
        mRatingChangeListener = ratingChangeListener;
    }

    public boolean isShowColors() {
        return mShowColors;
    }

    public void setShowColors(boolean showColors) {
        mShowColors = showColors;
    }

    public interface OnRatingsChangeListener {

        void onRatingsChanged(int rating);
    }
}
