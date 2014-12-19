package com.delectable.mobile.ui.capture.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureState;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.CommentRatingRowView;
import com.delectable.mobile.ui.common.widget.RatingTextView;
import com.delectable.mobile.ui.common.widget.WineBannerView;
import com.delectable.mobile.ui.wineprofile.viewmodel.VintageWineInfo;
import com.delectable.mobile.ui.wineprofile.widget.WinePriceView;
import com.delectable.mobile.util.DateHelperUtil;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

public class CaptureDetailsView extends RelativeLayout {

    private static final String TAG = CaptureDetailsView.class.getSimpleName();

    @InjectView(R.id.wine_banner)
    protected WineBannerView mWineBannerView;

    @InjectView(R.id.tagged_participants_container)
    protected View mTaggedParticipantsContainer;

    @InjectViews({R.id.tagged_user_image1, R.id.tagged_user_image2, R.id.tagged_user_image3})
    protected List<CircleImageView> mTaggedParticipantImages;

    @InjectView(R.id.capturer_comments_container)
    protected RelativeLayout mCapturerCommentsContainer;

    @InjectView(R.id.capturer_container)
    protected View mCapturerContainer;

    @InjectView(R.id.profile_image)
    protected CircleImageView mProfileImage2;

    @InjectView(R.id.user_name)
    protected TextView mUserName;

    @InjectView(R.id.influencer_badge)
    protected ImageView mInfluencerBadge;

    @InjectView(R.id.influencer_title)
    protected TextView mInfluencerTitle;

    @InjectView(R.id.user_comment)
    protected TextView mUserComment;

    @InjectView(R.id.capturer_rating)
    protected RatingTextView mCapturerRating;

    @InjectView(R.id.tagged_participants)
    protected TextView mTaggedParticipants;

    @InjectView(R.id.expanded_comments_container)
    protected View mExpandedCommentsContainer;

    @InjectView(R.id.participants_likes_divider)
    protected View mParticipantsLikesDivider;

    @InjectView(R.id.likes_text)
    protected TextView mLikesText;

    @InjectView(R.id.likes_comments_divider)
    protected View mLikesCommentsDivider;

    @InjectView(R.id.participants_comments_ratings_container)
    protected LinearLayout mParticipantsCommentsRatingsContainer;

    @InjectView(R.id.collapsed_comments_container)
    protected View mCollapsedCommentsContainer;

    @InjectView(R.id.likes_count)
    protected TextView mLikesCount;

    @InjectView(R.id.vertical_divider_likes)
    protected View mLikesDivider;

    @InjectView(R.id.comments_count)
    protected TextView mCommentsCount;

    @InjectView(R.id.vertical_divider_comments)
    protected View mCommentsDivider;

    @InjectView(R.id.rate_button)
    protected View mRateButton;

    @InjectView(R.id.rate_button_details)
    protected View mRateButtonDetails;

    @InjectView(R.id.comment_button)
    protected View mCommentButton;

    @InjectView(R.id.like_button)
    protected View mLikeButton;

    @InjectView(R.id.menu_button)
    protected View mMenuButton;

    @InjectView(R.id.price_view)
    protected WinePriceView mPriceButton;

    private Context mContext;

    private boolean mIsShowingComments = false;

    private boolean mShowPurchase = false;

    private CaptureDetails mCaptureDetails;

    private CaptureActionsHandler mActionsHandler;

    private PopupMenu mPopupMenu;

    private MenuItem mMenuActionRecommend;

    private MenuItem mMenuActionEdit;

    private MenuItem mMenuActionFlag;

    private MenuItem mMenuActionRemove;

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
        ButterKnife.inject(this);

        PopupMenu.OnMenuItemClickListener popUpListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.capture_action_recommend:
                        mActionsHandler.shareCapture(mCaptureDetails);
                        return true;
                    case R.id.capture_action_edit:
                        mActionsHandler.editCapture(mCaptureDetails);
                        return true;
                    case R.id.capture_action_flag:
                        mActionsHandler.flagCapture(mCaptureDetails);
                        return true;
                    case R.id.capture_action_remove:
                        mActionsHandler.discardCaptureClicked(mCaptureDetails);
                        return true;
                }
                return false;
            }
        };

        mPopupMenu = new PopupMenu(mContext, mMenuButton);
        mPopupMenu.inflate(R.menu.capture_actions);
        mPopupMenu.setOnMenuItemClickListener(popUpListener);
        mMenuActionRecommend = mPopupMenu.getMenu().findItem(R.id.capture_action_recommend);
        mMenuActionEdit = mPopupMenu.getMenu().findItem(R.id.capture_action_edit);
        mMenuActionFlag = mPopupMenu.getMenu().findItem(R.id.capture_action_flag);
        mMenuActionRemove = mPopupMenu.getMenu().findItem(R.id.capture_action_remove);

        mPriceButton.setActionsCallback(new WinePriceView.WinePriceViewActionsCallback() {
            @Override
            public void onPriceCheckClicked(VintageWineInfo wineInfo) {
                mPriceButton.showLoading();
                mActionsHandler.checkPrice(mCaptureDetails, CaptureDetailsView.this);
            }

            @Override
            public void onPriceClicked(VintageWineInfo wineInfo) {
                mActionsHandler.launchPurchaseFlow(mCaptureDetails);
            }

            @Override
            public void onSoldOutClicked(VintageWineInfo wineInfo) {
                //do nothing
            }
        });
    }

    public WinePriceView getPriceButton() {
        return mPriceButton;
    }

    public void updateData(CaptureDetails captureDetails, boolean showComments,
            boolean showPurchase) {
        mShowPurchase = showPurchase;
        updateData(captureDetails, showComments);
    }

    public void updateData(CaptureDetails captureDetails, boolean showComments) {
        mIsShowingComments = showComments;
        updateData(captureDetails);
    }

    public void updateData(CaptureDetails captureDetails) {
        mCaptureDetails = captureDetails;
        setupTopWineDetails();
        setupUserCommentsRating();
        setupTaggedParticipants();
        if (mIsShowingComments) {
            setupExpandedComments();
        } else {
            setupCollapsedComments();
        }

        //handle price button state
        if (mShowPurchase) {
            mPriceButton.updateWithPriceInfo(new VintageWineInfo(captureDetails.getWineProfile()));

            //might be in the middle of price fetching, need to show spinner
            if (mCaptureDetails.isTransacting() && CaptureDetails.TRANSACTION_KEY_PRICE
                    .equals(mCaptureDetails.getTransactionKey())) {
                mPriceButton.showLoading();
            }
        }

        setupActionButtonStates();
        setupPopUpMenu();
        mCapturerCommentsContainer.setVisibility(View.VISIBLE);
    }

    public void setActionsHandler(CaptureActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    private void setupPopUpMenu() {
        //first set everything to visible
        mMenuActionRecommend.setVisible(true);
        mMenuActionEdit.setVisible(true);
        mMenuActionFlag.setVisible(true);
        mMenuActionRemove.setVisible(true);

        boolean isOwnCapture = mCaptureDetails.getCapturerParticipant().getId()
                .equals(UserInfo.getUserId(mContext));
        CaptureState captureState = CaptureState.getState(mCaptureDetails);
        boolean isUnidentified =
                captureState == CaptureState.UNIDENTIFIED
                        || captureState == CaptureState.IMPOSSIBLED;

        if (isUnidentified) {
            mMenuActionRecommend.setVisible(false);
        }
        if (!isOwnCapture) {
            mMenuActionEdit.setVisible(false);
            mMenuActionFlag.setVisible(false);
            mMenuActionRemove.setVisible(false);
        }
    }

    private void setupTopWineDetails() {
        String wineImageUrl = mCaptureDetails.getPhoto().get450Plus();
        if (wineImageUrl == null) {
            wineImageUrl = mCaptureDetails.getPhoto().getUrl();
        }
        String producerName = mCaptureDetails.getDisplayTitle();
        String wineName = mCaptureDetails.getDisplayDescription();

        mWineBannerView.updateData(mCaptureDetails);

        mWineBannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.launchWineProfile(mCaptureDetails);
            }
        });
    }

    private void setupTaggedParticipants() {
        // TODO: Combine data from other participants objects
        final ArrayList<AccountMinimal> taggedParticipants =
                mCaptureDetails.getRegisteredParticipants() != null
                        ? mCaptureDetails.getRegisteredParticipants()
                        : new ArrayList<AccountMinimal>();

        boolean hasCaptureParticipants = taggedParticipants.size() > 0;

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
                mTaggedParticipantImages.get(i).setVisibility(View.GONE);
            }
        }

        // TODO: Figure out why taggedParticipants doesn't have more than 3 items, when the response has more than 3...
        // this doesn't work yet
        if (hasCaptureParticipants) {
            mTaggedParticipants.setVisibility(View.VISIBLE);

            String firstParticipant = taggedParticipants.get(0).getFname();
            String withText = taggedParticipants.size() == 2
                    ? mContext.getResources()
                    .getString(R.string.cap_feed_with_two, firstParticipant)
                    : mContext.getResources()
                            .getQuantityString(R.plurals.cap_feed_with, taggedParticipants.size(),
                                    firstParticipant,
                                    taggedParticipants.size() - 1);

            mTaggedParticipants.setText(withText);
            mTaggedParticipantsContainer.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActionsHandler.launchTaggedUsersListing(mCaptureDetails.getId());
                        }
                    }
            );
        }
    }

    private void setupUserCommentsRating() {
        String profileImageUrl = getThumbnailParticipantPhotoFromAccount(
                mCaptureDetails.getCapturerParticipant());
        String userName = "";
        String userComment = "";
        String captureTimeLocation = "";
        String userAccountId = "";

        // Signed in User comments
        if (mCaptureDetails.getCapturerParticipant() != null) {
            userName = mCaptureDetails.getCapturerParticipant().getFullName();
            userAccountId = mCaptureDetails.getCapturerParticipant().getId();
        }

        // Display the first user comment on top
        ArrayList<CaptureComment> userCaptureComments = mCaptureDetails
                .getCommentsForUserId(userAccountId);
        if (userCaptureComments.size() > 0) {
            userComment = userCaptureComments.get(0).getComment();
        }

        String time = DateHelperUtil.getPrettyTimePastOnly(mCaptureDetails.getCreatedAtDate());

        if (!userComment.isEmpty()) {
            captureTimeLocation =
                    (mCaptureDetails.getLocationName() != null && !mCaptureDetails.getLocationName()
                            .isEmpty())
                            ? getResources().getString(R.string.cap_feed_at_location_time,
                            mCaptureDetails.getLocationName(), time)
                            : getResources().getString(R.string.cap_feed_at_time, time);
        } else {
            captureTimeLocation =
                    (mCaptureDetails.getLocationName() != null && !mCaptureDetails.getLocationName()
                            .isEmpty())
                            ? getResources()
                            .getString(R.string.cap_feed_no_comment,
                                    mCaptureDetails.getLocationName(),
                                    time)
                            : getResources()
                                    .getString(R.string.cap_feed_no_comment_no_location, time);
        }
        SpannableString spannableString = SpannableString
                .valueOf((!userComment.isEmpty())
                        ? (userComment + " " + captureTimeLocation)
                        : captureTimeLocation);
        spannableString
                .setSpan(new ForegroundColorSpan(getResources().getColor(R.color.d_medium_gray)),
                        userComment.length(), spannableString.length(), 0);
        mUserComment.setText(spannableString, TextView.BufferType.SPANNABLE);

        ImageLoaderUtil.loadImageIntoView(mContext, profileImageUrl, mProfileImage2);

        final String finalUserAccountId = userAccountId;
        OnClickListener clickListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionsHandler.launchUserProfile(finalUserAccountId);
                    }
                };
        mCapturerContainer.setOnClickListener(clickListener);
        mProfileImage2.setOnClickListener(clickListener);

        mUserName.setText(userName);

        if (mCaptureDetails.getCapturerParticipant().isInfluencer()) {
            mInfluencerBadge.setVisibility(View.VISIBLE);
            String influencerTitle = mCaptureDetails.getCapturerParticipant()
                    .getInfluencerTitlesString();
            if (!influencerTitle.trim().isEmpty()) {
                mInfluencerTitle.setText(influencerTitle);
                mInfluencerTitle.setVisibility(View.VISIBLE);
            } else {
                mInfluencerTitle.setVisibility(View.GONE);
            }
        } else {
            mInfluencerBadge.setVisibility(View.GONE);
            mInfluencerTitle.setVisibility(View.GONE);
        }

        int rating = mCaptureDetails.getRatingForId(userAccountId);
        if (rating > -1 && !mShowPurchase) {
            mCapturerRating.setVisibility(View.VISIBLE);
            mCapturerRating.setRatingOf40(rating);
        } else {
            mCapturerRating.setVisibility(View.GONE);
        }

    }

    // Shows the likes and comments summary which expands to the full comments once clicked
    private void setupCollapsedComments() {
        mExpandedCommentsContainer.setVisibility(View.GONE);
        mCollapsedCommentsContainer.setVisibility(View.VISIBLE);

        OnClickListener expandLikesAndCommentsClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionsHandler.launchCaptureDetails(mCaptureDetails);
            }
        };

        // Likes
        int numLikes = mCaptureDetails.getLikesCount();
        if (numLikes > 0) {
            mLikesCount.setVisibility(View.VISIBLE);
            String likesCountText = mContext.getResources()
                    .getQuantityString(R.plurals.cap_feed_likes_count, numLikes, numLikes);
            mLikesCount.setText(likesCountText);
            mLikesCount.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // launch activity with liking people
                    mActionsHandler.launchLikingUsersListing(mCaptureDetails);
                }
            });
        } else {
            mLikesCount.setVisibility(View.GONE);
        }

        // Comments
        int numComments = mCaptureDetails.getCommentsCount();
        if (numComments > 0) {
            mCommentsCount.setVisibility(View.VISIBLE);
            String commentsCountText = mContext.getResources()
                    .getQuantityString(R.plurals.cap_feed_comments_count, numComments, numComments);
            mCommentsCount.setText(commentsCountText);
            mCommentsCount.setOnClickListener(expandLikesAndCommentsClickListener);
        } else {
            mCommentsCount.setVisibility(View.GONE);
        }

        // Rating button
        String currentUserId = UserInfo.getUserId(mContext);
        boolean isCurrentUserCapture = mCaptureDetails.getCapturerParticipant().getId()
                .equalsIgnoreCase(currentUserId);
        boolean isCurrentUserTaggedInCapture = mCaptureDetails.isUserTagged(currentUserId);
        boolean userHasRatedCapture = mCaptureDetails.getRatingForId(currentUserId) > -1;

        // Only show rating CTA if user hasn't rated already, and is tagged in capture
        mRateButton.setVisibility(
                ((isCurrentUserCapture || isCurrentUserTaggedInCapture) && !userHasRatedCapture)
                        ? View.VISIBLE
                        : View.GONE);

        // Vertical dividers
        mLikesDivider.setVisibility(
                (mLikesCount.getVisibility() == View.VISIBLE
                        && mCommentsCount.getVisibility() == View.VISIBLE)
                        ? View.VISIBLE
                        : View.GONE
        );
        mCommentsDivider.setVisibility(
                ((mCommentsCount.getVisibility() == View.VISIBLE
                        || mLikesCount.getVisibility() == View.VISIBLE)
                        && mRateButton.getVisibility() == View.VISIBLE)
                        ? View.VISIBLE
                        : View.GONE
        );

        // Hide container if nothing is in there
        if (mLikesCount.getVisibility() == View.GONE && mCommentsCount.getVisibility() == View.GONE
                && mRateButton.getVisibility() == View.GONE) {
            mCollapsedCommentsContainer.setVisibility(View.GONE);
        }

    }

    private void setupExpandedComments() {
        // hide collapsed likes and comments container
        mCollapsedCommentsContainer.setVisibility(View.GONE);
        mExpandedCommentsContainer.setVisibility(View.VISIBLE);

        // Likes
        mParticipantsLikesDivider.setVisibility(View.VISIBLE);
        mLikesCommentsDivider.setVisibility(View.VISIBLE);
        String likeText = "";
        int numLikes = mCaptureDetails.getLikesCount();
        if (numLikes == 0) {
            mLikesText.setVisibility(View.GONE);
            mParticipantsLikesDivider.setVisibility(View.GONE);
        } else if (numLikes == 1) {
            likeText = getResources().getString(R.string.cap_feed_like_text_one,
                    mCaptureDetails.getLikingParticipants().get(0).getFname());
        } else if (numLikes == 2) {
            likeText = getResources().getString(R.string.cap_feed_like_text_two,
                    mCaptureDetails.getLikingParticipants().get(0).getFname(),
                    mCaptureDetails.getLikingParticipants().get(1).getFname());
        } else if (numLikes >= 3) {
            likeText = getResources().getString(R.string.cap_feed_like_text_more,
                    mCaptureDetails.getLikingParticipants().get(0).getFname(),
                    mCaptureDetails.getLikingParticipants().get(1).getFname(),
                    numLikes - 2);
        }

        ForegroundColorSpan spanGray = new ForegroundColorSpan(
                getResources().getColor(R.color.d_medium_gray));
        SpannableString spannableString = SpannableString.valueOf(likeText);
        int positionAnd = likeText
                .lastIndexOf(getResources().getString(R.string.cap_feed_like_text_anchor_and));
        if (positionAnd >= 0) {
            spannableString.setSpan(spanGray, positionAnd, positionAnd + 3, 0);
        }
        int positionLiked = likeText
                .lastIndexOf(getResources().getString(R.string.cap_feed_like_text_anchor_like));
        if (positionLiked >= 0) {
            spannableString.setSpan(spanGray, positionLiked, likeText.length(), 0);
        }
        mLikesText.setText(spannableString, TextView.BufferType.SPANNABLE);
        mLikesText.setVisibility(View.VISIBLE);
        mLikesText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch activity with liking people
                mActionsHandler.launchLikingUsersListing(mCaptureDetails);
            }
        });

        // Comments
        mParticipantsCommentsRatingsContainer.removeAllViewsInLayout();
        CommentRatingRowView.LayoutParams layoutParams = new CommentRatingRowView.LayoutParams(
                CommentRatingRowView.LayoutParams.MATCH_PARENT,
                CommentRatingRowView.LayoutParams.WRAP_CONTENT);

        // TODO: Finalize Test out with feed with participants.
        // TODO: Show multiple comments for user
        AccountMinimal capturingAccount = mCaptureDetails.getCapturerParticipant();
        ArrayList<AccountMinimal> participants = mCaptureDetails.getCommentingParticipants();
        int numDisplayedComments = 0;
        if (participants != null) {
            mParticipantsCommentsRatingsContainer.setVisibility(View.VISIBLE);
            for (AccountMinimal participant : participants) {
                ArrayList<CaptureComment> comments = mCaptureDetails.getCommentsForUserId(
                        participant.getId());
                int firstIndex = 0;
                int rating = mCaptureDetails.getRatingForId(participant.getId());
                // Skip first user comment by the user who captured, otherwise it will show as duplicate
                if (capturingAccount.getId().equalsIgnoreCase(participant.getId())) {
                    firstIndex = 1;
                    // Don't duplicate ratings
                    rating = -1;
                }
                String firstCommentText = comments.size() > firstIndex ? comments.get(firstIndex)
                        .getComment() : "";
                // TODO : Figure out how to layout multiple comments with ratings?
                if (!firstCommentText.isEmpty() || Float.compare(rating, 0.0f) > 0) {
                    CommentRatingRowView commentRow = new CommentRatingRowView(mContext,
                            mActionsHandler, participant.getId());
                    //TODO simply not showing rating, might want to simplify CommentRatingRowView
                    commentRow.setNameAndComment(participant.getFullName(), firstCommentText);
                    mParticipantsCommentsRatingsContainer.addView(commentRow,
                            layoutParams);
                    numDisplayedComments++;
                }
                for (int i = (firstIndex + 1); i < comments.size(); i++) {
                    CommentRatingRowView commentRow = new CommentRatingRowView(mContext,
                            mActionsHandler, participant.getId());
                    //TODO simply not showing rating, might want to simplify CommentRatingRowView
                    commentRow.setNameAndComment(participant.getFullName(),
                            comments.get(i).getComment());
                    mParticipantsCommentsRatingsContainer.addView(commentRow,
                            layoutParams);
                    numDisplayedComments++;
                }
            }
        }
        if (numDisplayedComments == 0) {
            mParticipantsCommentsRatingsContainer.setVisibility(View.GONE);
            mLikesCommentsDivider.setVisibility(View.GONE);
        }

        // Rating button
        String currentUserId = UserInfo.getUserId(mContext);
        boolean isCurrentUserCapture = mCaptureDetails.getCapturerParticipant().getId()
                .equalsIgnoreCase(currentUserId);
        boolean isCurrentUserTaggedInCapture = mCaptureDetails.isUserTagged(currentUserId);
        boolean userHasRatedCapture = mCaptureDetails.getRatingForId(currentUserId) > -1;

        // Only show rating CTA if user hasn't rated already, and is tagged in capture
        mRateButtonDetails.setVisibility(
                ((isCurrentUserCapture || isCurrentUserTaggedInCapture) && !userHasRatedCapture)
                        ? View.VISIBLE
                        : View.GONE);
    }

    private void setupActionButtonStates() {
        String currentUserId = UserInfo.getUserId(mContext);
        boolean userLikesCapture = mCaptureDetails.doesUserLikeCapture(currentUserId);

        mLikeButton.setSelected(userLikesCapture);

        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.rateAndCommentForCapture(mCaptureDetails);
            }
        });

        mRateButtonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.rateAndCommentForCapture(mCaptureDetails);
            }
        });

        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.writeCommentForCapture(mCaptureDetails);
            }
        });

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                mActionsHandler.toggleLikeForCapture(mCaptureDetails, v.isSelected());
            }
        });

        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupMenu.show();
            }
        });

        mMenuButton.setVisibility(mShowPurchase ? View.GONE : View.VISIBLE);
        mPriceButton.setVisibility(mShowPurchase ? View.VISIBLE : View.GONE);
    }

    private String getThumbnailParticipantPhotoFromAccount(AccountMinimal account) {
        String profileImageUrl = account.getPhoto().getBestThumb();
        if (profileImageUrl != null) {
            return profileImageUrl;
        }
        return "";
    }

    public static interface CaptureActionsHandler {

        public void writeCommentForCapture(CaptureDetails capture);

        public void rateAndCommentForCapture(CaptureDetails capture);

        public void toggleLikeForCapture(CaptureDetails capture, boolean liked);

        public void launchWineProfile(CaptureDetails capture);

        public void checkPrice(CaptureDetails capture, CaptureDetailsView view);

        public void launchPurchaseFlow(CaptureDetails capture);

        public void launchCaptureDetails(CaptureDetails captures);

        public void launchUserProfile(String userAccountId);

        public void launchTaggedUsersListing(String captureId);

        public void launchLikingUsersListing(CaptureDetails capture);

        public void discardCaptureClicked(CaptureDetails capture);

        public void editCapture(CaptureDetails capture);

        public void flagCapture(CaptureDetails capture);

        public void shareCapture(CaptureDetails capture);
    }
}
