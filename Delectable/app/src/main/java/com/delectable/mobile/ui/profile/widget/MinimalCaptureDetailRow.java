package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureState;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.common.widget.RatingTextView;
import com.delectable.mobile.util.DateHelperUtil;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MinimalCaptureDetailRow extends RelativeLayout {

    private static final String TAG = MinimalCaptureDetailRow.class.getSimpleName();

    private final ForegroundColorSpan DARK_GRAY_SPAN;

    @InjectView(R.id.wine_image)
    protected ImageView mWineImage;

    @InjectView(R.id.producer_name)
    protected FontTextView mProducerName;

    @InjectView(R.id.wine_name)
    protected FontTextView mWineName;

    @InjectView(R.id.rating_textview)
    protected RatingTextView mRating;

    @InjectView(R.id.capture_comment_tagged_text)
    protected FontTextView mCommentText;

    @InjectView(R.id.like_comment_count_container)
    protected RelativeLayout mCountTextsContainer;

    @InjectView(R.id.likes_count)
    protected FontTextView mLikesCount;

    @InjectView(R.id.comments_count)
    protected FontTextView mCommentsCount;

    @InjectView(R.id.like_comment_buttons_container)
    protected RelativeLayout mLikeCommentButtonsContainer;

    @InjectView(R.id.like_button)
    protected ImageView mLikeButton;

    @InjectView(R.id.comment_button)
    protected ImageView mCommentButton;

    @InjectView(R.id.add_rating_remove_text_container)
    protected RelativeLayout mAddRatingRemoveTextContainer;

    @InjectView(R.id.add_rating_textview)
    protected TextView mAddRatingTextView;

    @InjectView(R.id.remove_textview)
    protected TextView mRemoveRatingTextView;

    @InjectView(R.id.overflow_button)
    protected ImageView mOverflowButton;

    private CaptureDetails mCaptureData;

    private String mSelectedUserId;

    private boolean mHasComment;

    private boolean mHasRating;

    private boolean mIsLoggedInUsersCapture;

    private boolean mUserIsCapturer;

    private CaptureDetailsView.CaptureActionsHandler mCaptureDetailsActionsHandler;

    private PopupMenu mPopupMenu;

    private PopupMenu mPopupMenuOwn;

    private PopupMenu mPopupMenuOther;

    public MinimalCaptureDetailRow(Context context) {
        this(context, null);
    }

    public MinimalCaptureDetailRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MinimalCaptureDetailRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        DARK_GRAY_SPAN = new ForegroundColorSpan(getResources().getColor(R.color.d_dark_gray));
        View.inflate(context, R.layout.row_minimal_capture_detail, this);
        ButterKnife.inject(this);

        PopupMenu.OnMenuItemClickListener popUpListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.capture_action_recommend:
                        mCaptureDetailsActionsHandler.shareCapture(mCaptureData);
                        return true;
                    case R.id.capture_action_edit:
                        mCaptureDetailsActionsHandler.editCapture(mCaptureData);
                        return true;
                    case R.id.capture_action_flag:
                        mCaptureDetailsActionsHandler.flagCapture(mCaptureData);
                        return true;
                    case R.id.capture_action_remove:
                        mCaptureDetailsActionsHandler.discardCaptureClicked(mCaptureData);
                        return true;
                }
                return false;
            }
        };

        mPopupMenuOwn = new PopupMenu(context, mOverflowButton);
        mPopupMenuOwn.inflate(R.menu.capture_actions_own);
        mPopupMenuOwn.setOnMenuItemClickListener(popUpListener);
        mPopupMenuOther = new PopupMenu(context, mOverflowButton);
        mPopupMenuOther.inflate(R.menu.capture_actions);
        mPopupMenuOther.setOnMenuItemClickListener(popUpListener);
    }

    public void updateData(CaptureDetails capture, String userId) {
        mSelectedUserId = userId;
        mIsLoggedInUsersCapture = mSelectedUserId.equals(UserInfo.getUserId(getContext()));
        mCaptureData = capture;
        mHasComment = false;
        mHasRating = false;

        String capturerId = capture.getCapturerParticipant().getId();
        mUserIsCapturer = mSelectedUserId.equals(capturerId);

        mPopupMenu = mIsLoggedInUsersCapture ? mPopupMenuOwn : mPopupMenuOther;

        updateWineInfo();
        updateUserRating();
        updateCommentsAndTags();
        // Must be configured last
        updateButtonDisplay();
    }

    private void updateWineInfo() {

        String captureTitle;
        String captureName;

        CaptureState state = CaptureState.getState(mCaptureData);
        switch (state) {
            case IDENTIFIED:
                captureTitle = mCaptureData.getWineProfile().getProducerName();
                captureName = mCaptureData.getWineProfile().getName();
                break;
            case IMPOSSIBLED:
                captureTitle = "";
                captureName = mCaptureData.getTranscriptionErrorMessage();
                break;
            case UNVERIFIED:
                captureTitle = mCaptureData.getBaseWine().getProducerName();
                captureName = mCaptureData.getBaseWine().getName();
                break;
            case UNIDENTIFIED:
            default:
                captureTitle = getResources().getString(R.string.user_captures_id_in_progress);
                captureName = getResources().getString(R.string.user_captures_check_back);
                break;
        }
        String captureImageUrl = mCaptureData.getPhoto().getBestThumb();

        mProducerName.setText(captureTitle);
        mWineName.setText(captureName);
        ImageLoaderUtil.loadImageIntoView(getContext(), captureImageUrl, mWineImage);
    }

    private void updateUserRating() {
        float captureRating = mCaptureData.getRatingForId(mSelectedUserId);
        if (captureRating > 0.0f) {
            mRating.setVisibility(View.VISIBLE);
            mRating.setRatingOf40(captureRating);
            mHasRating = true;
        } else {
            mRating.setVisibility(View.GONE);
            mHasRating = false;
        }
    }

    private void updateCommentsAndTags() {
        CharSequence message = getDisplayMessage(mCaptureData, mSelectedUserId);
        mCommentText.setText(message);
        mHasComment = true;
    }

    /**
     * The display comment/tag message has 16 different variations depending on taggee, location,
     * whether viewing on profile, whether user is capturer or taggee. Hopefully nothing gets added
     * to this, because this was very annoying to figure out.
     */
    private CharSequence getDisplayMessage(CaptureDetails capture, String selectedUserId) {
        //first, figure out what capture has and what it's missing
        boolean hasTaggees = false;
        int taggeeCount = 0;

        boolean hasLocation = false;
        String location = null;

        //only showing registered participants, fb not shown bc fb api won't allow access to whole friends list soon
        if (!capture.getTaggeeParticipants().getRegistered().isEmpty()) {
            taggeeCount = capture.getTaggeeParticipants().getRegistered().size();
            hasTaggees = true;
        }

        if (capture.getLocationName() != null
                && !capture.getLocationName().equals("")) {
            hasLocation = true;
            location = capture.getLocationName();
        }

        String captureTime = DateHelperUtil.getPrettyTimePastOnly(capture.getCreatedAtDate());
        String capturerName = capture.getCapturerParticipant().getFullName();

        //Prepare message for viewing own captures / capture that meets show comment condition
        //conditions when viewing another user's captures will be handled further down
        String message = "";

        //4 possible conditions based on taggee and location values
        if (!hasTaggees && !hasLocation) {
            //only time
            message = captureTime;
        }

        if (!hasTaggees && hasLocation) {
            message = getResources()
                    .getString(R.string.at_yy_place_zz_time_ago, location, captureTime);
        }

        if (hasTaggees && !hasLocation) {
            message = getResources()
                    .getQuantityString(R.plurals.with_xx_friends_zz_time_ago, taggeeCount,
                            taggeeCount, captureTime);
        }

        if (hasTaggees && hasLocation) {
            message = getResources().getQuantityString(
                    R.plurals.with_xx_friends_at_yy_place_zz_time_ago, taggeeCount,
                    taggeeCount, location, captureTime);
        }

        //if user is capturer and has first comment, then we show the comment
        String userComment = null;
        if (mUserIsCapturer) {
            //if first comment is user's
            if (!capture.getComments().isEmpty()) {
                CaptureComment firstComment = capture.getComments().get(0);
                if (selectedUserId.equals(firstComment.getAccountId())) {
                    userComment = firstComment.getComment();
                }
            }
        }

        //handle case where comment exists first, append some extra information
        if (userComment != null) {
            SpannableString span = new SpannableString(userComment);
            span.setSpan(DARK_GRAY_SPAN, 0, span.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            return TextUtils.concat(span, " - ", message);
        }

        //we can straight return the message built above since these are user's captures
        if (mUserIsCapturer) {
            return message;
        }

        //from here down, if user is not capturer, it can only mean that user was tagged in this capture

        //handling viewing own captures case
        String userFirstName = "";

        if (mIsLoggedInUsersCapture) {
            userFirstName = getResources().getString(R.string.you);
        } else {
            AccountMinimal account = capture.getTaggeeParticipants()
                    .checkForRegisteredUser(selectedUserId);
            //really account should never be null
            if (account != null) {
                userFirstName = account.getFname();
            }
        }

        //4 possible conditions based on taggee and location values
        boolean hasOtherTaggees = taggeeCount > 1;
        int taggeeCountUserExcluded = taggeeCount - 1;
        if (!hasOtherTaggees && !hasLocation) {
            //just tag text and time
            message = getResources()
                    .getString(R.string.capturer_tagged_user_zz_time_ago, capturerName,
                            userFirstName,
                            captureTime);
        }

        if (!hasOtherTaggees && hasLocation) {
            message = getResources()
                    .getString(R.string.capturer_tagged_user_at_yy_place_zz_time_ago,
                            capturerName, userFirstName, location, captureTime);
        }

        if (hasOtherTaggees && !hasLocation) {
            message = getResources().getQuantityString(
                    R.plurals.capturer_tagged_user_and_xx_friends_zz_time_ago,
                    taggeeCountUserExcluded,
                    capturerName, userFirstName, taggeeCountUserExcluded, captureTime);
        }

        if (hasOtherTaggees && hasLocation) {
            message = getResources().getQuantityString(
                    R.plurals.capturer_tagged_user_and_xx_friends_at_yy_place_zz_time_ago,
                    taggeeCountUserExcluded,
                    capturerName, userFirstName, taggeeCountUserExcluded, location, captureTime);
        }

        return message;
    }

    private void updateButtonDisplay() {
        // default, views below comment are gone
        mAddRatingRemoveTextContainer.setVisibility(View.GONE);
        mCountTextsContainer.setVisibility(View.GONE);

        //hide like and comment buttons if viewing own user profile
        int visibility = mIsLoggedInUsersCapture ? View.GONE : View.VISIBLE;
        mLikeCommentButtonsContainer.setVisibility(visibility);

        //setup likes/comments counts
        int likes = mCaptureData.getLikesCount();
        int comments = mCaptureData.getComments().size();
        mLikesCount.setText(
                getResources().getQuantityString(R.plurals.likes_count, likes, likes));
        mCommentsCount.setText(
                getResources().getQuantityString(R.plurals.comments_count, comments, comments));

        if (!mIsLoggedInUsersCapture) { //looking at someone else's captures
            //always show this even when there are no likes/comments so user can click like/comment button and not have view jump
            mCountTextsContainer.setVisibility(View.VISIBLE);
            return;
        }

        //from here down, looking at your own feed
        if (!mUserIsCapturer) { //and you were tagged
            if (!mHasRating) { //and there was no rating
                mAddRatingRemoveTextContainer.setVisibility(View.VISIBLE);
                return;
            }
        }

        //show like and comment counts if at least one of them is populated
        if (mCaptureData.getLikesCount() > 0 || !mCaptureData.getComments().isEmpty()) {
            mCountTextsContainer.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.wine_image, R.id.wine_name, R.id.producer_name})
    protected void onWineDetailsClick() {
        if (mCaptureDetailsActionsHandler != null) {
            mCaptureDetailsActionsHandler.launchWineProfile(mCaptureData);
        }
    }

    @OnClick({R.id.likes_count, R.id.comments_count})
    protected void onLikesCommentsCountClick() {
        if (mCaptureDetailsActionsHandler != null) {
            mCaptureDetailsActionsHandler.launchCaptureDetails(mCaptureData);
        }
    }

    @OnClick(R.id.like_button)
    protected void onLikeClick() {
        if (mCaptureDetailsActionsHandler != null) {
            mCaptureDetailsActionsHandler.toggleLikeForCapture(mCaptureData);
        }
    }

    @OnClick(R.id.comment_button)
    protected void onCommentButtonClick() {
        if (mCaptureDetailsActionsHandler != null) {
            mCaptureDetailsActionsHandler.rateAndCommentForCapture(mCaptureData);
        }
    }

    @OnClick(R.id.add_rating_textview)
    protected void onAddRatingClick() {
        if (mCaptureDetailsActionsHandler != null) {
            mCaptureDetailsActionsHandler.rateAndCommentForCapture(mCaptureData);
        }
    }

    @OnClick(R.id.remove_textview)
    protected void onRemoveClick() {
        if (mCaptureDetailsActionsHandler != null) {
            mCaptureDetailsActionsHandler.discardCaptureClicked(mCaptureData);
        }
    }

    @OnClick(R.id.overflow_button)
    protected void onOverflowClick() {
        if (mCaptureDetailsActionsHandler != null) {
            mPopupMenu.show();
        }
    }

    public void setActionsHandler(
            CaptureDetailsView.CaptureActionsHandler actionsHandler) {
        mCaptureDetailsActionsHandler = actionsHandler;
    }
}
