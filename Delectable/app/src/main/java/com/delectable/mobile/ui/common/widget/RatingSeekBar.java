package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureMinimal;
import com.delectable.mobile.ui.common.drawable.RatingsBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

public class RatingSeekBar extends SeekBar implements SeekBar.OnSeekBarChangeListener,
        View.OnTouchListener {

    public static final int INCREMENTS = CaptureMinimal.MAX_RATING_VALUE;

    private RatingsBar mRatingsBar;

    private OnRatingsChangeListener mRatingChangeListener;

    private OnTouchListener mOnTouchListener;

    // Can toggle this to use bar colors
    private boolean mShowColors = false;

    // Rating percent, -1 won't show colors or anything
    private float mRatingPercent = -1.0f;

    public RatingSeekBar(Context context) {
        super(context);
        init();
    }

    public RatingSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setMax(INCREMENTS);

        int barHeight = getResources().getDimensionPixelSize(R.dimen.rating_bar_seek_height);
        mRatingsBar = new RatingsBar(getContext(), barHeight);
        setProgressDrawable(mRatingsBar);
        Drawable thumb = getResources().getDrawable(R.drawable.btn_rating_bar_slider_normal);
        setThumb(thumb);

        setOnSeekBarChangeListener(this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mRatingChangeListener != null) {
                    mRatingChangeListener.onStartTrackingTouch(this);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mRatingChangeListener != null) {
                    mRatingChangeListener.onStopTrackingTouch(this);
                }
                break;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser || mShowColors) {
            mRatingPercent = progress / 40.0f;
            updateRatingBarColors();
        }
        if (mRatingChangeListener != null) {
            mRatingChangeListener.onRatingsChanged(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mRatingChangeListener != null) {
            mRatingChangeListener.onStartTrackingTouch(seekBar);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mRatingChangeListener != null) {
            mRatingChangeListener.onStopTrackingTouch(seekBar);
        }
    }

    private void updateRatingBarColors() {
        if (mRatingPercent >= 0) {
            mRatingsBar.setPercent(mRatingPercent);
        }
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

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        RatingsSavedState ss = new RatingsSavedState(superState);

        ss.ratingsPercent = mRatingPercent;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        RatingsSavedState ss = (RatingsSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mRatingPercent = ss.ratingsPercent;
        updateRatingBarColors();
    }

    public interface OnRatingsChangeListener {

        void onRatingsChanged(int rating);

        void onStartTrackingTouch(SeekBar seekBar);

        void onStopTrackingTouch(SeekBar seekBar);
    }

    static class RatingsSavedState extends BaseSavedState {

        public static final Parcelable.Creator<RatingsSavedState> CREATOR
                = new Parcelable.Creator<RatingsSavedState>() {
            public RatingsSavedState createFromParcel(Parcel in) {
                return new RatingsSavedState(in);
            }

            public RatingsSavedState[] newArray(int size) {
                return new RatingsSavedState[size];
            }
        };

        float ratingsPercent;

        RatingsSavedState(Parcelable superState) {
            super(superState);
        }

        private RatingsSavedState(Parcel in) {
            super(in);
            ratingsPercent = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeFloat(ratingsPercent);
        }
    }
}
