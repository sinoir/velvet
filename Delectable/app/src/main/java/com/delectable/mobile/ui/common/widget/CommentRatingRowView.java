package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.FontEnum;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Row for Tagged user comment and/or rating in the capture Feed
 */
public class CommentRatingRowView extends RelativeLayout {

    private FontTextView mNameCommentTextView;

    private RatingsBarView mRatingsBarView;

    public CommentRatingRowView(Context context) {
        this(context, null);
    }

    public CommentRatingRowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentRatingRowView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int verticalSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                24, metrics);
        float textSize = getResources().getDimensionPixelSize(R.dimen.cap_feed_comment_text_size);
        int ratingViewHeight = getResources().getDimensionPixelSize(R.dimen.rating_bar_height);

        // Setup View
        LayoutParams textViewParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        LayoutParams ratingViewParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                ratingViewHeight);

        mNameCommentTextView = new FontTextView(context);
        mNameCommentTextView.setTypeface(FontEnum.WHITNEY_BOOK);
        mNameCommentTextView.setTextColor(getResources().getColor(R.color.d_medium_gray));
        textViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(mNameCommentTextView, textViewParams);
        mNameCommentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        mRatingsBarView = new RatingsBarView(context);
        ratingViewParams.addRule(RelativeLayout.BELOW, mNameCommentTextView.getId());
        ratingViewParams.setMargins(0, verticalSpacing, 0, 0);
        addView(mRatingsBarView, ratingViewParams);
        mRatingsBarView.setVisibility(View.GONE);
    }

    public void setNameAndComment(String name, String comment) {
        String text = name + ": " + comment;
        SpannableString spannableString = SpannableString.valueOf(text);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), 0);
        spannableString
                .setSpan(new ForegroundColorSpan(getResources().getColor(R.color.d_dark_gray)), 0,
                        name.length() + 1, 0);
        mNameCommentTextView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    public void setNameCommentWithRating(String name, String comment, float ratingPercent) {
        setNameAndComment(name, comment);
        if (Float.compare(ratingPercent, 0.0f) > 0) {
            mRatingsBarView.setVisibility(View.VISIBLE);
            mRatingsBarView.setPercent(ratingPercent);
        } else {
            mRatingsBarView.setVisibility(View.GONE);
        }
    }

    public void setNameWithRating(String name, float ratingPercent) {
        setNameCommentWithRating(name, "", ratingPercent);
    }
}
