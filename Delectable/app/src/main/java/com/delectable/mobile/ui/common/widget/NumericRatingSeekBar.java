package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.Animate;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
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

        mRatingSeekBar.setOnRatingChangeListener(new RatingSeekBar.OnRatingsChangeListener() {
            @Override
            public void onRatingsChanged(int rating) {
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
                Animate.fadeInVertical(mHappyFaceScoreContainer);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Animate.fadeOutVertical(mHappyFaceScoreContainer, 1000);
            }
        });
    }

    public RatingSeekBar getRatingSeekBar() {
        return mRatingSeekBar;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener listener) {
        mListener = listener;
    }


}
