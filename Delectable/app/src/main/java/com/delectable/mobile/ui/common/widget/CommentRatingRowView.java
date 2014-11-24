package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Row for Tagged user comment and/or rating in the capture Feed
 */
public class CommentRatingRowView extends RelativeLayout {

    private FontTextView mNameCommentTextView;

    private RatingTextView mRatingTextView;

    public CommentRatingRowView(Context context) {
        this(context, null);
    }

    public CommentRatingRowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentRatingRowView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_comment_rating, this);

        mNameCommentTextView = (FontTextView) findViewById(R.id.comment_text);
        mRatingTextView = (RatingTextView) findViewById(R.id.comment_rating);
        mRatingTextView.setVisibility(View.GONE);
    }

    public void setNameAndComment(String name, String comment) {
        String text = name + (comment.isEmpty() ? "" : ": ") + comment;
        SpannableString spannableString = SpannableString.valueOf(text);
        if (name != null && !name.isEmpty()) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), 0);
//            spannableString
//                    .setSpan(new ForegroundColorSpan(getResources().getColor(R.color.d_dark_gray)),
//                            0,
//                            name.length() + (comment.isEmpty() ? 0 : 1), 0);
        }
        mNameCommentTextView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    public void setNameCommentWithRating(String name, String comment, int rating) {
        setNameAndComment(name, comment);
        if (rating > -1) {
            mRatingTextView.setVisibility(View.VISIBLE);
            mRatingTextView.setRatingOf40(rating);
        } else {
            mRatingTextView.setVisibility(View.GONE);
        }
    }

    public void setNameWithRating(String name, int rating) {
        setNameCommentWithRating(name, "", rating);
    }
}
