package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.HashtagMentionSpan;
import com.delectable.mobile.ui.common.widget.RatingTextView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A representation of the {@link CaptureNote} model, this is the row for the listview that appears
 * in the Wine Profile screen.
 */
public class WineProfileCommentUnitRow extends RelativeLayout {

    private final ForegroundColorSpan MEDIUM_GRAY_SPAN;

    @InjectView(R.id.profile_image)
    protected CircleImageView mProfileImage;

    @InjectView(R.id.user_name)
    protected TextView mUserName;

    @InjectView(R.id.influencer_badge)
    protected ImageView mInfluencerBadge;

    @InjectView(R.id.influencer_titles)
    protected TextView mInfluencerTitles;

    @InjectView(R.id.rating)
    protected RatingTextView mRatingTextView;

    @InjectView(R.id.user_comment)
    protected TextView mUserComment;

    @InjectView(R.id.helpful_count)
    protected TextView mHelpfulCount;

    @InjectView(R.id.helpful_button)
    protected ImageButton mHelpfulButton;

    private ActionsHandler mActionsHandler;

    private CaptureNote mCaptureNote;

    public WineProfileCommentUnitRow(Context context) {
        this(context, null);
    }

    public WineProfileCommentUnitRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WineProfileCommentUnitRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        MEDIUM_GRAY_SPAN = new ForegroundColorSpan(getResources().getColor(R.color.d_medium_gray));
        View.inflate(context, R.layout.row_wine_profile_comment_unit, this);
        ButterKnife.inject(this);

        mUserComment.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick({R.id.profile_image, R.id.capturer_container})
    public void launchUserProfile() {
        String userId = mCaptureNote.getCapturerParticipant().getId();
        mActionsHandler.launchUserProfile(userId);
    }

    @OnClick(R.id.helpful_button)
    protected void onHelpfulClick(View v) {
        v.setSelected(!v.isSelected());

        if (v.isSelected()) {
            mCaptureNote.markHelpful(UserInfo.getUserId(getContext()));
        } else {
            mCaptureNote.unmarkHelpful(UserInfo.getUserId(getContext()));
        }
        updateHelpfulViews(mCaptureNote);
        mActionsHandler.toggleHelpful(mCaptureNote, v.isSelected());
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void updateData(CaptureNote captureNote) {
        mCaptureNote = captureNote;
        AccountMinimal capturer = captureNote.getCapturerParticipant();

        //user image
        //load empty photo first, so user doesn't see old photo flash when rows recycle
        ImageLoaderUtil.loadImageIntoView(getContext(), R.drawable.no_photo, mProfileImage);
        String profileImageUrl = capturer.getPhoto().getBestThumb();
        ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mProfileImage);

        //user name
        mUserName.setText(capturer.getFullName());

        //influencer title(s)
        if (capturer.isInfluencer()) {
            mInfluencerBadge.setVisibility(View.VISIBLE);

            String titles = capturer.getInfluencerTitlesString();
            if (titles.equals("")) {
                mInfluencerTitles.setVisibility(View.GONE);
            } else {
                mInfluencerTitles.setText(titles);
                mInfluencerTitles.setVisibility(View.VISIBLE);
            }
        } else {
            //not an influencer
            mInfluencerTitles.setVisibility(View.GONE);
            mInfluencerBadge.setVisibility(View.GONE);
        }

        //capture comment
        CharSequence message;
        if (captureNote.getNote() == null || captureNote.getNote().isEmpty()) {
            message = getResources()
                    .getString(R.string.wine_profile_empty_comment_message, capturer.getFname(),
                            captureNote.getVintage());
        } else {
            //color the vintage text medium gray
            String vintage = getResources()
                    .getString(R.string.wine_profile_vintage, captureNote.getVintage());
            SpannableString span = new SpannableString(vintage);
            span.setSpan(MEDIUM_GRAY_SPAN, 0, span.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            SpannableString commentSpan = new SpannableString(captureNote.getNote());
            // hashtag and mention spans
            ArrayList<CaptureCommentAttributes> attributes = captureNote.getCommentAttributes();
            HashtagMentionSpan.applyHashtagAndMentionSpans(getContext(), commentSpan, attributes);
            message = TextUtils.concat(commentSpan, span);
        }

        mUserComment.setText(message);

        //rating
        if (captureNote.getCapturerRating() >= 0) {
            mRatingTextView.setVisibility(View.VISIBLE);
            mRatingTextView.setRatingOf40(captureNote.getCapturerRating());
        } else {
            mRatingTextView.setVisibility(View.GONE);
        }

        updateHelpfulViews(captureNote);
    }

    private void updateHelpfulViews(CaptureNote captureNote) {
        //helpful count
        int helpfulCount = captureNote.getHelpfulingAccountIds().size();
        if (helpfulCount > 0) {
            mHelpfulCount.setVisibility(View.VISIBLE);
            mHelpfulCount.setText(getResources()
                    .getQuantityString(R.plurals.wine_profile_helpful_count, helpfulCount,
                            helpfulCount));
        } else {
            mHelpfulCount.setVisibility(View.GONE);
        }

        //whether user marked it as helpful
        boolean markedHelpful = captureNote.getHelpfulingAccountIds()
                .contains(UserInfo.getUserId(getContext()));
        mHelpfulButton.setSelected(markedHelpful);
    }

    public static interface ActionsHandler {

        public void toggleHelpful(CaptureNote captureNote, boolean markHelpful);

        public void launchUserProfile(String userAccountId);
    }


}
