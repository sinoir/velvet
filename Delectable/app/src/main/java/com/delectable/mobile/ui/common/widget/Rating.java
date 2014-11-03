package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

public enum Rating {
    EXQUISITE(30.0f, 9.0f, 0.75f, R.color.d_rating_exquisite, R.drawable.ic_ratingbar_best),
    GOOD(20.0f, 8.0f, 0.5f, R.color.d_rating_good, R.drawable.ic_ratingbar_good),
    OK(10.0f, 7.0f, 0.25f, R.color.d_rating_ok, R.drawable.ic_ratingbar_mediocre),
    BAD(0.0f, 6.0f, 0f, R.color.d_rating_bad, R.drawable.ic_ratingbar_terrible),
    NO_RATING(-1.0f, -1.0f, -1.0f, R.color.d_light_gray, R.color.transparent);

    private float mStartValueOfFourty;

    private float mStartValueOfTen;

    private float mStartPercent;

    private int mColorRes;



    private int mHappyFaceRes;

    private Rating(float startValue40, float startValue10, float startPercent, int colorRes, int happyFaceRes) {
        mStartValueOfFourty = startValue40;
        mStartValueOfTen = startValue10;
        mStartPercent = startPercent;
        mColorRes = colorRes;
        mHappyFaceRes = happyFaceRes;
    }

    public float getStartValueOfFourty() {
        return mStartValueOfFourty;
    }

    public float getStartValueOfTen() {
        return mStartValueOfTen;
    }

    public float getStartPercent() {
        return mStartPercent;
    }

    public int getColorRes() {
        return mColorRes;
    }

    public int getHappyFaceRes() {
        return mHappyFaceRes;
    }

    public static Rating valueForPercentage(float rating) {
        if (Float.compare(rating, 0.0f) < 0) {
            return NO_RATING;
        }
        for (Rating ratingItem : values()) {
            if (Float.compare(rating, ratingItem.getStartPercent()) >= 0) {
                return ratingItem;
            }
        }
        //code should never reach this point
        return Rating.NO_RATING;
    }

    public static Rating valueForRatingOfTen(float rating) {
        if (rating < 0.0f) {
            return NO_RATING;
        }
        for (Rating ratingItem : values()) {
            if (rating >= ratingItem.getStartValueOfTen()) {
                return ratingItem;
            }
        }
        //if for some reason rating was lower than 6.0 and higher than 0, thought this shouldn't happen
        return Rating.NO_RATING;
    }

    public static Rating valueForRatingOfFourty(float rating) {
        if (rating < 0.0f) {
            return NO_RATING;
        }
        for (Rating ratingItem : values()) {
            if (rating >= ratingItem.getStartValueOfFourty()) {
                return ratingItem;
            }
        }
        //code should never reach this point
        return Rating.NO_RATING;
    }

    public static float getRatingOfTenFrom40(float ratingOf40) {
        //if negative, return that value means no rating
        if (ratingOf40 < 0.0f) {
            return ratingOf40;
        }
        return (ratingOf40 + 60.0f) / 10.0f;
    }

}
