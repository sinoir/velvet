package com.delectable.mobile.util;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.Rating;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.text.DecimalFormat;

public class TextUtil {

    private static final int NO_AVG_RATING = -1;

    private static final DecimalFormat ONE_DECIMAL_SPACE = new DecimalFormat("0.0");

    private static final RelativeSizeSpan RATING_BIG_TEXT_SPAN = new RelativeSizeSpan(1.3f);

    /**
     * Makes the rating display text where the rating is a bit bigger than the 10.
     */
    public static CharSequence makeRatingDisplayText(Context context, double ratingOf40) {
        //handling a no rating case, show a dash
        //we need to set it's size, otherwise the text will become unaligned if there are only user ratings and no pro ratings or vice versa
        if (ratingOf40 == NO_AVG_RATING) {
            String dash = "-";
            SpannableString span = new SpannableString(dash);
            ForegroundColorSpan GRAY_COLOR = new ForegroundColorSpan(
                    context.getResources().getColor(R.color.d_medium_gray));
            span.setSpan(RATING_BIG_TEXT_SPAN, 0, dash.length(), 0); // set size
            span.setSpan(GRAY_COLOR, 0, dash.length(), 0); // set color
            return span;
        }

        double ratingOf10 = Rating.getRatingOfTenFrom40(ratingOf40);
        String ratingStr = ONE_DECIMAL_SPACE.format(ratingOf10);
        //10.0 always displays as 10
        if (ratingStr.equals("10.0")) {
            ratingStr = "10";
        }
        Rating ratingItem = Rating.valueForRatingOfFourty(ratingOf40);

        ForegroundColorSpan COLOR = new ForegroundColorSpan(
                context.getResources().getColor(ratingItem.getColorRes()));
        ForegroundColorSpan COLOR2 = new ForegroundColorSpan(
                context.getResources().getColor(ratingItem.getColorRes()));

        SpannableString ratingSpan = new SpannableString(ratingStr);
        ratingSpan.setSpan(RATING_BIG_TEXT_SPAN, 0, ratingStr.length(), 0); // set size
        ratingSpan.setSpan(COLOR, 0, ratingStr.length(), 0); // set color

        String of10 = "/10";
        SpannableString of10Span = new SpannableString(of10);
        of10Span.setSpan(COLOR2, 0, of10.length(), 0); // set color

        CharSequence displayText = TextUtils.concat(ratingSpan, of10Span);
        return displayText;
    }

}
