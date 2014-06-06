package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.text.Html;
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

    private TextView mNameCommentTextView;

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
                10, metrics);
        float textSize = getResources().getDimensionPixelSize(R.dimen.cap_feed_comment_text_size);
        int ratingViewHeight = getResources().getDimensionPixelSize(R.dimen.rating_bar_height);

        // Setup View
        LayoutParams textViewParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        LayoutParams ratingViewParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                ratingViewHeight);

        mNameCommentTextView = new TextView(context);
        mNameCommentTextView.setId(1);
        textViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(mNameCommentTextView, textViewParams);
        mNameCommentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        mRatingsBarView = new RatingsBarView(context);
        mRatingsBarView.setId(2);
        ratingViewParams.addRule(RelativeLayout.BELOW, mNameCommentTextView.getId());
        ratingViewParams.setMargins(0, verticalSpacing, 0, 0);
        addView(mRatingsBarView, ratingViewParams);
        mRatingsBarView.setVisibility(View.GONE);

    }

    public void setNameAndComment(String name, String comment) {
        String text = name + ": <font color='#606060'>" + comment + "</font>";
        mNameCommentTextView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
    }

    public void setNameCommentWithRating(String name, String comment, float ratingPercent) {
        setNameAndComment(name, comment);
        mRatingsBarView.setVisibility(View.VISIBLE);
        mRatingsBarView.setPercent(ratingPercent);
    }

    public void setNameWithRating(String name, float ratingPercent) {
        setNameCommentWithRating(name, "", ratingPercent);
    }
}
