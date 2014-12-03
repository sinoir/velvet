package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Wraps a numeric rating view around a bare RatingSeekBar. Handles showing and hiding of the
 * numeric view as well.
 */
public class NumericRatingSeekBar extends RelativeLayout {

    /**
     * Simple interface to pass up rating changed information from {@link RatingSeekBar}
     */
    public interface OnRatingChangeListener {

        public void onRatingsChanged(int rating);
    }

    private static final int HAPPY_FACE_TRANSLATION = 50;

    @InjectView(R.id.happy_face_score_container)
    protected RelativeLayout mHappyFaceScoreContainer;

    @InjectView(R.id.rating_happy_face)
    protected ImageView mHappyFace;

    @InjectView(R.id.score_textview)
    protected FontTextView mScoreTextView;

    @InjectView(R.id.rate_seek_bar)
    protected RatingSeekBar mRatingSeekBar;

    private OnRatingChangeListener mListener;

    private Handler mHandler = new Handler();

    public NumericRatingSeekBar(Context context) {
        this(context, null);
    }

    public NumericRatingSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumericRatingSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.numeric_rating_bar, this);
        ButterKnife.inject(this);

        mHappyFaceScoreContainer.setTranslationY(HAPPY_FACE_TRANSLATION);

        mRatingSeekBar.setOnRatingChangeListener(new RatingSeekBar.OnRatingsChangeListener() {
            @Override
            public void onRatingsChanged(int rating) {
                rating++; //offset to make rating scale is 6.1-10 instead of 6.0-9.9

                double ratingOf10 = Rating.getRatingOfTenFrom40(rating);
                String ratingStr = Rating.formatOf10Rating(ratingOf10);
                mScoreTextView.setText(ratingStr);

                Rating ratingItem = Rating.valueForRatingOfTen(ratingOf10);
                int color = getResources().getColor(ratingItem.getColorRes());
                mScoreTextView.setTextColor(color);
                mHappyFace.setImageResource(ratingItem.getHappyFaceRes());

                if (mListener != null) {
                    mListener.onRatingsChanged(rating);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(null);
                showHappyFace();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideHappyFace();
                    }
                }, 1000);
            }
        });
    }

    private void showHappyFace() {
        mHappyFaceScoreContainer.clearAnimation();
        mHappyFaceScoreContainer.animate()
                .alpha(1)
                .setDuration(100)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void hideHappyFace() {
        mHappyFaceScoreContainer.clearAnimation();
        mHappyFaceScoreContainer.animate()
                .alpha(0)
                .translationY(HAPPY_FACE_TRANSLATION)
//                .setStartDelay(600)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    public RatingSeekBar getRatingSeekBar() {
        return mRatingSeekBar;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener listener) {
        mListener = listener;
    }


}
