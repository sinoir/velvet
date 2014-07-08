package com.delectable.mobile.ui.capture.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.CommentRatingRowView;
import com.delectable.mobile.ui.common.widget.RatingsBarView;
import com.delectable.mobile.util.ImageLoaderUtil;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CaptureDetailsView extends RelativeLayout {

    private ImageView mWineImage;

    private TextView mProducerName;

    private TextView mWineName;

    private View mTaggedParticipantsContainer;

    private CircleImageView mProfileImage1;

    private ArrayList<CircleImageView> mTaggedParticipantImages;

    private ImageView mMoreTaggedParticipantsButton;

    private RelativeLayout mCapturerCommentsContainer;

    private CircleImageView mProfileImage2;

    private TextView mUserName;

    private TextView mUserComment;

    private TextView mCaptureTimeLocation;

    private RatingsBarView mUserCaptureRatingBar;

    private LinearLayout mParticipantsCommentsRatingsContainer;

    private View mRateButton;

    private View mCommentButton;

    private View mLikeButton;

    private TextView mLikesCount;

    private View mMenuButton;

    private Context mContext;

    private CaptureDetails mCaptureData;

    private CaptureActionsHandler mActionsHandler;

    public CaptureDetailsView(Context context) {
        this(context, null);
    }

    public CaptureDetailsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureDetailsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        View.inflate(context, R.layout.row_feed_wine_detail, this);

        // Top Wine Section
        mWineImage = (ImageView) findViewById(R.id.wine_image);
        mProducerName = (TextView) findViewById(R.id.producer_name);
        mWineName = (TextView) findViewById(R.id.wine_name);

        // Tagged Participants Section
        mTaggedParticipantsContainer = findViewById(R.id.tagged_participants_container);
        mProfileImage1 = (CircleImageView) findViewById(R.id.profile_image1);
        mTaggedParticipantImages = new ArrayList<CircleImageView>();
        mTaggedParticipantImages.add((CircleImageView) findViewById(R.id.tagged_user_image1));
        mTaggedParticipantImages.add((CircleImageView) findViewById(R.id.tagged_user_image2));
        mTaggedParticipantImages.add((CircleImageView) findViewById(R.id.tagged_user_image3));
        mMoreTaggedParticipantsButton = (ImageView) findViewById(R.id.more_tagged_user_button);

        // User Comment/Rating
        mCapturerCommentsContainer = (RelativeLayout) findViewById(
                R.id.capturer_comments_container);
        mProfileImage2 = (CircleImageView) findViewById(R.id.profile_image2);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserComment = (TextView) findViewById(R.id.user_comment);
        mCaptureTimeLocation = (TextView) findViewById(R.id.capture_time_location);
        mUserCaptureRatingBar = (RatingsBarView) findViewById(R.id.capturer_rating_bar);

        // Participants Comment/Rating
        mParticipantsCommentsRatingsContainer = (LinearLayout) findViewById(
                R.id.participants_comments_ratings_container);

        // Action buttons:
        mRateButton = findViewById(R.id.rate_button);
        mCommentButton = findViewById(R.id.comment_button);
        mLikeButton = findViewById(R.id.like_button);
        mLikesCount = (TextView) findViewById(R.id.likes_count);
        mMenuButton = findViewById(R.id.menu_button);
    }

    public void updateCaptureData(CaptureDetails captureData) {
        mCaptureData = captureData;
        setupTopWineDetails();
        setupTaggedParticipants();
        setupUserCommentsRating();
        setupParticipantsRatingsAndComments();
        setupActionButtonStates();
        mCapturerCommentsContainer.setVisibility(View.VISIBLE);
    }

    public void setActionsHandler(CaptureActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    private void setupTopWineDetails() {
        String wineImageUrl = mCaptureData.getPhoto().getUrl();
        String producerName = mCaptureData.getDisplayTitle();
        String wineName = mCaptureData.getDisplayDescription();

        ImageLoaderUtil.loadImageIntoView(mContext, wineImageUrl, mWineImage);
        mProducerName.setText(producerName);
        mWineName.setText(wineName);

        mWineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.launchWineProfile(mCaptureData.getWineProfile());
            }
        });
    }

    private void setupTaggedParticipants() {
        String profileImageUrl = getThumbnailParticipantPhotoFromAccount(
                mCaptureData.getCapturerParticipant());

        // TODO: Combine data from other participants objects
        final ArrayList<Account> taggedParticipants =
                mCaptureData.getRegisteredParticipants() != null
                        ? mCaptureData.getRegisteredParticipants() : new ArrayList<Account>();

        boolean hasCaptureParticipants = taggedParticipants.size() > 0;

        ImageLoaderUtil.loadImageIntoView(mContext, profileImageUrl, mProfileImage1);
        mProfileImage1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCaptureData.getCapturerParticipant() != null) {
                            mActionsHandler
                                    .launchUserProfile(
                                            mCaptureData.getCapturerParticipant().getId());
                        }
                    }
                }
        );

        if (hasCaptureParticipants) {
            mTaggedParticipantsContainer.setVisibility(View.VISIBLE);
        } else {
            mTaggedParticipantsContainer.setVisibility(View.GONE);
        }

        for (int i = 0; i < mTaggedParticipantImages.size(); i++) {
            if (taggedParticipants.size() > i) {
                String imageUrl = getThumbnailParticipantPhotoFromAccount(
                        taggedParticipants.get(i));
                final String accountId = taggedParticipants.get(i).getId();
                ImageLoaderUtil.loadImageIntoView(mContext, imageUrl,
                        mTaggedParticipantImages.get(i));
                mTaggedParticipantImages.get(i).setVisibility(View.VISIBLE);
                mTaggedParticipantImages.get(i).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActionsHandler.launchUserProfile(accountId);
                            }
                        }
                );
            } else {
                mTaggedParticipantImages.get(i).setVisibility(View.INVISIBLE);
            }
        }

        if (taggedParticipants.size() == 3) {
            // TODO: Add Touchstate to Open more tagged profile listing
            mMoreTaggedParticipantsButton.setVisibility(View.VISIBLE);
            mMoreTaggedParticipantsButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Ideally pass up list of tagged users, not the full mCaptureData object
                            mActionsHandler.launchTaggedUserListing(mCaptureData);
                        }
                    }
            );
        } else {
            mMoreTaggedParticipantsButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setupUserCommentsRating() {
        String profileImageUrl = getThumbnailParticipantPhotoFromAccount(
                mCaptureData.getCapturerParticipant());
        String userName = "";
        String userComment = "";
        String captureTimeLocation = "";
        String userAccountId = "";
        float capturePercent = 0.0f;

        // Signed in User comments
        if (mCaptureData.getCapturerParticipant() != null) {
            userName = mCaptureData.getCapturerParticipant().getFullName();
            userAccountId = mCaptureData.getCapturerParticipant().getId();
        }

        // Display the first user comment on top
        ArrayList<CaptureComment> userCaptureComments = mCaptureData
                .getCommentsForUserId(userAccountId);
        if (userCaptureComments.size() > 0) {
            userComment = userCaptureComments.get(0).getComment();
        }

        capturePercent = mCaptureData.getRatingPercentForId(userAccountId);

        PrettyTime p = new PrettyTime();
        captureTimeLocation = p.format(mCaptureData.getCreatedAtDate());

        // TODO: Add Location once location we find capture with location...
        String location = "";
        captureTimeLocation += location;

        ImageLoaderUtil.loadImageIntoView(mContext, profileImageUrl, mProfileImage2);

        final String finalUserAccountId = userAccountId;
        mProfileImage2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionsHandler.launchUserProfile(finalUserAccountId);
                    }
                }
        );

        mUserName.setText(userName);
        if (userComment != "") {
            mUserComment.setText(userComment);
            mUserComment.setVisibility(View.VISIBLE);
        } else {
            mUserComment.setText("");
            mUserComment.setVisibility(View.GONE);
        }
        if (capturePercent > 0.0f) {
            mUserCaptureRatingBar.setVisibility(View.VISIBLE);
            mUserCaptureRatingBar.setPercent(capturePercent);
        } else {
            mUserCaptureRatingBar.setVisibility(View.GONE);
        }

        mCaptureTimeLocation.setText(captureTimeLocation);
    }

    // Shows the rest of the comments/ratings below the first user comment
    private void setupParticipantsRatingsAndComments() {
        mParticipantsCommentsRatingsContainer.removeAllViewsInLayout();
        CommentRatingRowView.LayoutParams layoutParams = new CommentRatingRowView.LayoutParams(
                CommentRatingRowView.LayoutParams.MATCH_PARENT,
                CommentRatingRowView.LayoutParams.WRAP_CONTENT);
        int verticalSpacing = mContext.getResources()
                .getDimensionPixelSize(R.dimen.cap_feed_row_small_vertical_spacing);

        // TODO: Finalize Test out with feed with participants.
        // TODO: Show multiple comments for user
        Account capturingAccount = mCaptureData.getCapturerParticipant();
        ArrayList<Account> participants = mCaptureData.getCommentingParticipants();
        int numDisplayedComments = 0;
        if (participants != null) {
            mParticipantsCommentsRatingsContainer.setVisibility(View.VISIBLE);
            for (Account participant : participants) {
                ArrayList<CaptureComment> comments = mCaptureData.getCommentsForUserId(
                        participant.getId());
                int firstIndex = 0;
                float rating = mCaptureData.getRatingPercentForId(participant.getId());
                // Skip first user comment by the user who captured, otherwise it will show as duplicate
                if (capturingAccount.getId().equalsIgnoreCase(participant.getId())) {
                    firstIndex = 1;
                    // Don't duplicate ratings
                    rating = -1.0f;
                }
                String firstCommentText = comments.size() > firstIndex ? comments.get(firstIndex)
                        .getComment() : "";
                // TODO : Figure out how to layout multiple comments with ratings?
                if (firstCommentText != "" || rating > 0.0f) {
                    CommentRatingRowView commentRow = new CommentRatingRowView(mContext);
                    commentRow.setPadding(0, verticalSpacing, 0, verticalSpacing);
                    commentRow.setNameCommentWithRating(participant.getFullName(), firstCommentText,
                            rating);
                    mParticipantsCommentsRatingsContainer.addView(commentRow,
                            layoutParams);
                    numDisplayedComments++;
                }
                for (int i = (firstIndex + 1); i < comments.size(); i++) {
                    CommentRatingRowView commentRow = new CommentRatingRowView(mContext);
                    commentRow.setPadding(0, verticalSpacing, 0, verticalSpacing);
                    commentRow.setNameCommentWithRating(participant.getFullName(),
                            comments.get(i).getComment(), -1);
                    mParticipantsCommentsRatingsContainer.addView(commentRow,
                            layoutParams);
                    numDisplayedComments++;
                }
            }
        }
        if (numDisplayedComments == 0) {
            mParticipantsCommentsRatingsContainer.setVisibility(View.GONE);
        }
    }

    private void setupActionButtonStates() {
        int numLikes = mCaptureData.getLikesCount();
        String likesCountText = mContext.getString(R.string.cap_feed_likes_count, numLikes);
        mLikesCount.setText(likesCountText);
        String userId = UserInfo.getUserId(mContext);
        boolean userLikesCapture = mCaptureData.doesUserLikeCapture(userId);
        boolean isCurrentUserCapture = mCaptureData.getCapturerParticipant().getId()
                .equalsIgnoreCase(userId);

        // Rating can only be done for user's capture:
        if (isCurrentUserCapture) {
            mRateButton.setVisibility(View.VISIBLE);
        } else {
            mRateButton.setVisibility(View.GONE);
        }

        mLikeButton.setSelected(userLikesCapture);

        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.rateAndCommentForCapture(mCaptureData);
            }
        });

        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.writeCommentForCapture(mCaptureData);
            }
        });

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.toggleLikeForCapture(mCaptureData);
            }
        });

        // TODO : Menu Overlfow Does what?
    }

    private String getThumbnailParticipantPhotoFromAccount(Account account) {
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


    public static interface CaptureActionsHandler {

        public void writeCommentForCapture(CaptureDetails capture);

        public void rateAndCommentForCapture(CaptureDetails capture);

        public void toggleLikeForCapture(CaptureDetails capture);

        public void launchWineProfile(WineProfile wineProfile);

        public void launchUserProfile(String userAccountId);

        public void launchTaggedUserListing(CaptureDetails capture);
    }
}
