package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Row for Tagged user comment and/or rating in the capture Feed
 */
public class CommentRatingRowView extends RelativeLayout {

    private CaptureDetailsView.CaptureActionsHandler mActionsHandler;

    private FontTextView mNameCommentTextView;

    private RatingTextView mRatingTextView;

    private String mUserAccountId;

    public CommentRatingRowView(Context context,
            CaptureDetailsView.CaptureActionsHandler actionsHandler, String userAccountId) {
        this(context);
        mActionsHandler = actionsHandler;
        mUserAccountId = userAccountId;
    }

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
        mNameCommentTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mRatingTextView = (RatingTextView) findViewById(R.id.comment_rating);
        mRatingTextView.setVisibility(View.GONE);
    }

    public void setNameAndComment(String name, String comment,
            ArrayList<CaptureCommentAttributes> attributes) {
        String text = name + (comment.isEmpty() ? "" : ": ") + comment;
        SpannableString spannableString = SpannableString.valueOf(text);
        if (name != null && !name.isEmpty()) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    name.length() + (comment.isEmpty() ? 0 : 1),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new TouchableSpan(mNameCommentTextView.getCurrentTextColor()) {
                @Override
                public void onClick(View view) {
                    if (mActionsHandler != null) {
                        mActionsHandler.launchUserProfile(mUserAccountId);
                    }
                }
            }, 0, name.length() + (comment.isEmpty() ? 0 : 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString
//                    .setSpan(new ForegroundColorSpan(getResources().getColor(R.color.d_dark_gray)),
//                            0,
//                            name.length() + (comment.isEmpty() ? 0 : 1), 0);
        }
        if (attributes != null && !attributes.isEmpty()) {
            for (CaptureCommentAttributes a : attributes) {
                int tagStart = a.getRange().get(0) + (text.length() - comment
                        .length()); // offset for name in front of comment
                int tagEnd = tagStart + a.getRange().get(1);
                String tag = spannableString.subSequence(tagStart, tagEnd).toString();
                spannableString.setSpan(
                        new HashtagMentionSpan(tag, a.getLink(), a.getType()),
                        tagStart, tagEnd,
                        Spanned.SPAN_COMPOSING);
            }
        }
        mNameCommentTextView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }
/*
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
*/

}
