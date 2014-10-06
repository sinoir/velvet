package com.delectable.mobile.ui.wineprofile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.RatingsBarView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A representation of the {@link CaptureNote} model, this is the row for the listview that appears
 * in the Wine Profile screen.
 */
public class WineProfileCommentUnitRow extends RelativeLayout {

    private CircleImageView mProfileImage;

    private TextView mUserName;

    private TextView mInfluencerTitles;

    private TextView mUserComment;

    private RatingsBarView mRatingsBar;

    private TextView mHelpfulCount;

    private ImageButton mHelpfulButton;

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

        View.inflate(context, R.layout.row_wine_profile_comment_unit, this);

        mProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        mUserName = (TextView) findViewById(R.id.user_name);
        mInfluencerTitles = (TextView) findViewById(R.id.influencer_titles);
        mUserComment = (TextView) findViewById(R.id.user_comment);
        mRatingsBar = (RatingsBarView) findViewById(R.id.rating_bar);
        mHelpfulCount = (TextView) findViewById(R.id.helpful_count);
        mHelpfulButton = (ImageButton) findViewById(R.id.helpful_button);

        OnClickListener launchUserProfileClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mCaptureNote.getCapturerParticipant().getId();
                mActionsHandler.launchUserProfile(userId);
            }
        };
        mProfileImage.setOnClickListener(launchUserProfileClickListener);
        mUserName.setOnClickListener(launchUserProfileClickListener);
        mInfluencerTitles.setOnClickListener(launchUserProfileClickListener);

        mHelpfulButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());

                //TODO caching might need to be done here
                //currently this is only modifying the reference in memory and not performing any sort of syncing
                if (v.isSelected()) {
                    mCaptureNote.markHelpful(UserInfo.getUserId(getContext()));
                } else {
                    mCaptureNote.unmarkHelpful(UserInfo.getUserId(getContext()));
                }
                updateHelpfulViews(mCaptureNote);
                mActionsHandler.toggleHelpful(mCaptureNote, v.isSelected());
            }
        });
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void updateCaptureNoteData(CaptureNote captureNote) {
        mCaptureNote = captureNote;
        AccountMinimal capturer = captureNote.getCapturerParticipant();

        //user image
        String profileImageUrl = getThumbnailParticipantPhotoFromAccount(capturer);
        ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mProfileImage);

        //user name
        mUserName.setText(capturer.getFullName());

        //influencer title(s)
        if (capturer.isInfluencer()) {
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
        }

        //capture comment
        String message;
        if (captureNote.getNote() == null || captureNote.getNote().equals("")) {
            message = getResources()
                    .getString(R.string.wine_profile_empty_comment_message, capturer.getFname(),
                            captureNote.getVintage());
        } else {
            //TODO vintage string needs to be made medium gray
            String vintage = getResources()
                    .getString(R.string.wine_profile_vintage, captureNote.getVintage());
            message = captureNote.getNote() + vintage;
        }
        mUserComment.setText(message);

        //rating
        if (captureNote.getRatingPercent() > 0.0f) {
            mRatingsBar.setVisibility(View.VISIBLE);
            mRatingsBar.setPercent(captureNote.getRatingPercent());
        } else {
            mRatingsBar.setVisibility(View.GONE);
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


    //TODO: possibly make this a static global method, this is copied from CaptureDetailsView
    private String getThumbnailParticipantPhotoFromAccount(AccountMinimal account) {
        String profileImageUrl = "";
        if (account.getPhoto() != null) {
            if (account.getPhoto().getThumbUrl() != null) {
                profileImageUrl = account.getPhoto().getThumbUrl();
            } else {
                profileImageUrl = account.getPhoto().getUrl();
            }
        }
        return profileImageUrl;
    }

    public static interface ActionsHandler {

        public void toggleHelpful(CaptureNote captureNote, boolean markHelpful);

        public void launchUserProfile(String userAccountId);
    }


}
