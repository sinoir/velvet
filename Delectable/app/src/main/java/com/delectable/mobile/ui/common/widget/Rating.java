package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

public enum Rating {
    EXQUISITE(30.0d, 9.0d, 0.75d, R.color.d_rating_exquisite, R.drawable.ic_ratingbar_best),
    GOOD(20.0d, 8.0d, 0.5d, R.color.d_rating_good, R.drawable.ic_ratingbar_good),
    OK(10.0d, 7.0d, 0.25d, R.color.d_rating_ok, R.drawable.ic_ratingbar_mediocre),
    BAD(0.0d, 6.0d, 0d, R.color.d_rating_bad, R.drawable.ic_ratingbar_terrible),
    NO_RATING(-1.0d, -1.0d, -1.0d, R.color.d_light_gray, R.color.transparent);

    private double mStartValueOfFourty;

    private double mStartValueOfTen;

    private double mStartPercent;

    private int mColorRes;



    private int mHappyFaceRes;

    private Rating(double startValue40, double startValue10, double startPercent, int colorRes, int happyFaceRes) {
        mStartValueOfFourty = startValue40;
        mStartValueOfTen = startValue10;
        mStartPercent = startPercent;
        mColorRes = colorRes;
        mHappyFaceRes = happyFaceRes;
    }

    public double getStartValueOfFourty() {
        return mStartValueOfFourty;
    }

    public double getStartValueOfTen() {
        return mStartValueOfTen;
    }

    public double getStartPercent() {
        return mStartPercent;
    }

    public int getColorRes() {
        return mColorRes;
    }

    public int getHappyFaceRes() {
        return mHappyFaceRes;
    }

    public static Rating valueForPercentage(double rating) {
        if (Double.compare(rating, 0.0d) < 0) {
            return NO_RATING;
        }
        for (Rating ratingItem : values()) {
            if (Double.compare(rating, ratingItem.getStartPercent()) >= 0) {
                return ratingItem;
            }
        }
        //code should never reach this point
        return Rating.NO_RATING;
    }

    public static Rating valueForRatingOfTen(double rating) {
        if (rating < 0.0d) {
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

    public static Rating valueForRatingOfFourty(double rating) {
        if (rating < 0.0d) {
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

    public static double getRatingOfTenFrom40(double ratingOf40) {
        //if negative, return that value means no rating
        if (ratingOf40 < 0.0d) {
            return ratingOf40;
        }
        return (ratingOf40 + 60.0f) / 10.0f;
    }

}
