package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import java.text.DecimalFormat;

public class RatingTextView extends FontTextView{

    GradientDrawable mBg;

    public RatingTextView(Context context) {
        super(context);
        init();
    }

    public RatingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBg = (GradientDrawable)getResources().getDrawable(R.drawable.bg_numeric_rating);
        setBackgroundDrawable(mBg);
    }

    public void setRatingOf40(float rating) {
        float ratingOf10 = Rating.getRatingOfTenFrom40(rating);
        setRatingOf10(ratingOf10);
    }

    public void setRatingOf10(float ratingOf10) {
        DecimalFormat df = new DecimalFormat("0.0");
        String ratingStr = df.format(ratingOf10);
        if (ratingStr.equals("10.0")) {
            setText("10");
        } else {
            setText(ratingStr);
        }

        Rating rating = Rating.valueForRatingOfTen(ratingOf10);
        mBg.setColor(getResources().getColor(rating.getColorRes()));
    }
}
