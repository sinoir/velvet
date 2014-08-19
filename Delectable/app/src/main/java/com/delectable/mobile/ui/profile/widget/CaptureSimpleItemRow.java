package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.common.widget.RatingsBarView;
import com.delectable.mobile.util.ImageLoaderUtil;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CaptureSimpleItemRow extends RelativeLayout {

    private static final String TAG = CaptureSimpleItemRow.class.getSimpleName();

    @InjectView(R.id.wine_image)
    protected ImageView mWineImage;

    @InjectView(R.id.producer_name)
    protected TextView mProducerName;

    @InjectView(R.id.wine_name)
    protected TextView mWineName;

    @InjectView(R.id.capture_comment_tagged_text)
    protected TextView mCommentText;

    @InjectView(R.id.rate_comment)
    protected TextView mRateComment;

    @InjectView(R.id.rating_bar)
    protected RatingsBarView mRatingBarView;

    @InjectView(R.id.capture_time)
    protected TextView mCaptureTime;

    @InjectView(R.id.compose_capture)
    protected View mComposeCapture;

    @InjectView(R.id.discard_capture)
    protected View mDiscardCapture;

    private Context mContext;

    private CaptureDetails mCaptureData;

    private String mSelectedUserId;

    private boolean mHasComment;

    private boolean mHasRating;

    private boolean mIsLoggedInUsersCapture;

    private CaptureDetailsView.CaptureActionsHandler mActionsHandler;


    public CaptureSimpleItemRow(Context context) {
        this(context, null);
    }

    public CaptureSimpleItemRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureSimpleItemRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;

        View.inflate(context, R.layout.row_simple_wine_detail, this);
        ButterKnife.inject(this);
    }

    public void updateData(CaptureDetails capture, String userId) {
        mSelectedUserId = userId;
        mIsLoggedInUsersCapture = mSelectedUserId.equals(UserInfo.getUserId(mContext));
        mCaptureData = capture;
        mSelectedUserId = userId;
        mHasComment = false;
        mHasRating = false;

        updateWineInfo();
        updateUserRating();
        updateCommentsAndTags();
        updateCaptureTime();
        // Must be configured last
        updateButtonDisplay();
    }

    private void updateWineInfo() {
        String captureTitle = mCaptureData.getDisplayTitle();
        String captureName = mCaptureData.getDisplayDescription();
        String captureImageUrl = mCaptureData.getPhoto().getThumbUrl();
        if (captureImageUrl == null || captureImageUrl.equals("")) {
            captureImageUrl = mCaptureData.getPhoto().getUrl();
        }

        mProducerName.setText(captureTitle);
        mWineName.setText(captureName);
        ImageLoaderUtil.loadImageIntoView(getContext(), captureImageUrl, mWineImage);
    }

    private void updateUserRating() {
        float capturePercent = mCaptureData.getRatingPercentForId(mSelectedUserId);
        if (capturePercent > 0.0f) {
            mRatingBarView.setVisibility(View.VISIBLE);
            mRatingBarView.setPercent(capturePercent);
            mHasRating = true;
        } else {
            mRatingBarView.setVisibility(View.GONE);
            mHasRating = false;
        }
    }

    private void updateCommentsAndTags() {
        String userComment = "";
        String userAccountId = "";

        // Signed in User comments
        if (mCaptureData.getCapturerParticipant() != null) {
            userAccountId = mCaptureData.getCapturerParticipant().getId();
        }

        // Display the first user comment on top
        ArrayList<CaptureComment> userCaptureComments = mCaptureData
                .getCommentsForUserId(userAccountId);
        if (userCaptureComments.size() > 0) {
            userComment = userCaptureComments.get(0).getComment();
        }

        String location = "";
        if (mCaptureData.getLocationName() != null) {
            location = getResources()
                    .getString(R.string.cap_feed_at_location, mCaptureData.getLocationName());
        }

        // Number of Taggees
        String withFriends = "";
        int numTaggees = mCaptureData.getNumberTaggedParticipants();
        if (numTaggees > 0) {
            withFriends = getResources()
                    .getQuantityString(R.plurals.with_friends, numTaggees, numTaggees);
        }

        if (location.length() > 0 || withFriends.length() > 0) {
            userComment += " - " + location;
            // Add space between location and tagged friends count if location exists.
            if (location.length() > 0) {
                userComment += " ";
            }
            userComment += withFriends;
        }

        if (userComment != "") {
            mCommentText.setText(userComment);
            mCommentText.setVisibility(View.VISIBLE);
            mHasComment = true;
        } else {
            mCommentText.setText("");
            mCommentText.setVisibility(View.GONE);
            mHasComment = false;
        }
    }

    private void updateCaptureTime() {
        PrettyTime p = new PrettyTime();
        String captureTime = p.format(mCaptureData.getCreatedAtDate());
        mCaptureTime.setText(captureTime);
    }

    private void updateButtonDisplay() {
        // TODO: Figure out way to see if Capture is matched scan? ...
        boolean isScanned = true;

        // Default, buttons are "GONE"
        mRateComment.setVisibility(View.GONE);
        mComposeCapture.setVisibility(View.GONE);
        mDiscardCapture.setVisibility(View.GONE);

        if (mIsLoggedInUsersCapture) {
            mDiscardCapture.setVisibility(View.VISIBLE);
        }

        // If not scanned, don't show buttons.
        if (!isScanned) {
            return;
        }

        // Scanned, not rated and commented yet
        if (!mHasComment && !mHasRating) {
            mRateComment.setVisibility(View.VISIBLE);
            mRateComment.setText(R.string.cap_feed_add_rating_and_comment);
        }

        // If scanned, and no rating exists, show the add rating text
        if (mHasComment && !mHasRating) {
            mRateComment.setVisibility(View.VISIBLE);
            mRateComment.setText(R.string.cap_feed_add_your_rating);
        }

        // If scanned, and has a rating or a comment, show the compose/edit button
        if (mIsLoggedInUsersCapture && (mHasComment || mHasRating)) {
            mComposeCapture.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.rate_comment)
    protected void rateCommentClicked() {
        if (mActionsHandler != null) {
            mActionsHandler.rateAndCommentForCapture(mCaptureData);
        }
    }

    @OnClick(R.id.discard_capture)
    protected void discardCaptureClicked() {
        if (mActionsHandler != null) {
            mActionsHandler.discardCapture(mCaptureData);
        }
    }

    @OnClick(R.id.compose_capture)
    protected void composeClicked() {
        if (mActionsHandler != null) {
            mActionsHandler.editCapture(mCaptureData);
        }
    }

    @OnClick({R.id.wine_image, R.id.wine_name, R.id.producer_name})
    protected void wineDetailsClicked() {
        if (mActionsHandler != null) {
            mActionsHandler.launchWineProfile(mCaptureData);
        }
    }

    public void setActionsHandler(CaptureDetailsView.CaptureActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }
}
