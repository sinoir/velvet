package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.Capture;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.util.ImageLoaderUtil;

import org.ocpsoft.prettytime.PrettyTime;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FollowFeedAdapter extends BaseAdapter {

    private static final String TAG = "FollowFeedAdapter";

    private Activity mContext;

    private ArrayList<CaptureDetails> mData;

    private FeedItemActionsHandler mActionsHandler;

    public FollowFeedAdapter(Activity context, ArrayList<CaptureDetails> data,
            FeedItemActionsHandler actionsHandler) {
        mContext = context;
        mData = data;
        mActionsHandler = actionsHandler;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Capture getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getFeedView(position, convertView, parent);
    }

    private View getFeedView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        FeedViewHolder viewHolder = null;
        CaptureDetails capture = mData.get(position);

        if (rowView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.row_feed_wine_detail, null);
            viewHolder = new FeedViewHolder();
            // Top Wine Section
            viewHolder.wineImage = (ImageView) rowView.findViewById(R.id.wine_image);
            viewHolder.producerName = (TextView) rowView.findViewById(R.id.producer_name);
            viewHolder.wineName = (TextView) rowView.findViewById(R.id.wine_name);

            // Tagged Participants Section
            viewHolder.taggedParticipantsContainer = rowView
                    .findViewById(R.id.tagged_participants_container);
            viewHolder.profileImage1 = (CircleImageView) rowView.findViewById(R.id.profile_image1);
            viewHolder.taggedParticipantImages = new ArrayList<CircleImageView>();
            viewHolder.taggedParticipantImages
                    .add((CircleImageView) rowView.findViewById(R.id.tagged_user_image1));
            viewHolder.taggedParticipantImages
                    .add((CircleImageView) rowView.findViewById(R.id.tagged_user_image2));
            viewHolder.taggedParticipantImages
                    .add((CircleImageView) rowView.findViewById(R.id.tagged_user_image3));
            viewHolder.moreTaggedParticipantsButton = (ImageView) rowView
                    .findViewById(R.id.more_tagged_user_button);

            // User Comment/Rating
            viewHolder.profileImage2 = (CircleImageView) rowView.findViewById(R.id.profile_image2);
            viewHolder.userName = (TextView) rowView.findViewById(R.id.user_name);
            viewHolder.userComment = (TextView) rowView.findViewById(R.id.user_comment);
            viewHolder.captureTimeLocation = (TextView) rowView
                    .findViewById(R.id.capture_time_location);
            viewHolder.userCaptureRatingBar = (RatingsBarView) rowView
                    .findViewById(R.id.capturer_rating_bar);

            // Participants Comment/Rating
            viewHolder.participantsCommentsRatingsContainer = (LinearLayout) rowView
                    .findViewById(R.id.participants_comments_ratings_container);

            // Action buttons:
            viewHolder.rateButton = rowView.findViewById(R.id.rate_button);
            viewHolder.commentButton = rowView.findViewById(R.id.comment_button);
            viewHolder.likeButton = rowView.findViewById(R.id.like_button);
            viewHolder.likesCount = (TextView) rowView.findViewById(R.id.likes_count);
            viewHolder.menuButton = rowView.findViewById(R.id.menu_button);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (FeedViewHolder) rowView.getTag();
        }
        setupTopWineDetails(viewHolder, capture);
        setupTaggedParticipants(viewHolder, capture);
        setupUserCommentsRating(viewHolder, capture);
        setupParticipantsRatingsAndComments(viewHolder, capture);
        setupActionButtonStates(viewHolder, capture);

        return rowView;
    }

    private void setupTopWineDetails(FeedViewHolder viewHolder, final CaptureDetails capture) {
        String wineImageUrl = "";
        String producerName = "";
        String wineName = "";
        if (capture.getWineProfile() != null) {
            producerName = capture.getWineProfile().getProducerName();
            wineName = capture.getWineProfile().getName();
            wineImageUrl = capture.getPhoto().getUrl();
        }
        ImageLoaderUtil.loadImageIntoView(mContext, wineImageUrl, viewHolder.wineImage);
        viewHolder.producerName.setText(producerName);
        viewHolder.wineName.setText(wineName);

        viewHolder.wineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.launchWineProfile(capture.getWineProfile());
            }
        });
    }

    private void setupTaggedParticipants(FeedViewHolder viewHolder, final CaptureDetails capture) {
        String profileImageUrl = getThumbnailParticipantPhotoFromAccount(
                capture.getCapturerParticipant());

        // TODO: Combine data from other participants objects
        final ArrayList<Account> taggedParticipants = capture.getRegisteredParticipants() != null
                ? capture.getRegisteredParticipants() : new ArrayList<Account>();

        boolean hasCaptureParticipants = taggedParticipants.size() > 0;

        ImageLoaderUtil.loadImageIntoView(mContext, profileImageUrl, viewHolder.profileImage1);
        viewHolder.profileImage1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (capture.getCapturerParticipant() != null) {
                            mActionsHandler
                                    .launchUserProfile(capture.getCapturerParticipant().getId());
                        }
                    }
                }
        );

        if (hasCaptureParticipants) {
            viewHolder.taggedParticipantsContainer.setVisibility(View.VISIBLE);
        } else {
            viewHolder.taggedParticipantsContainer.setVisibility(View.GONE);
        }

        for (int i = 0; i < viewHolder.taggedParticipantImages.size(); i++) {
            if (taggedParticipants.size() > i) {
                String imageUrl = getThumbnailParticipantPhotoFromAccount(
                        taggedParticipants.get(i));
                final String accountId = taggedParticipants.get(i).getId();
                ImageLoaderUtil.loadImageIntoView(mContext, imageUrl,
                        viewHolder.taggedParticipantImages.get(i));
                viewHolder.taggedParticipantImages.get(i).setVisibility(View.VISIBLE);
                viewHolder.taggedParticipantImages.get(i).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActionsHandler.launchUserProfile(accountId);
                            }
                        }
                );
            } else {
                viewHolder.taggedParticipantImages.get(i).setVisibility(View.INVISIBLE);
            }
        }

        if (taggedParticipants.size() == 3) {
            // TODO: Add Touchstate to Open more tagged profile listing
            viewHolder.moreTaggedParticipantsButton.setVisibility(View.VISIBLE);
            viewHolder.moreTaggedParticipantsButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: Ideally pass up list of tagged users, not the full capture object
                            mActionsHandler.launchTaggedUserListing(capture);
                        }
                    }
            );
        } else {
            viewHolder.moreTaggedParticipantsButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setupUserCommentsRating(FeedViewHolder viewHolder, CaptureDetails capture) {
        String profileImageUrl = getThumbnailParticipantPhotoFromAccount(
                capture.getCapturerParticipant());
        String userName = "";
        String userComment = "";
        String captureTimeLocation = "";
        String userAccountId = "";
        float capturePercent = 0.0f;

        if (capture.getCapturerParticipant() != null) {
            userName = capture.getCapturerParticipant().getFullName();
            userAccountId = capture.getCapturerParticipant().getId();
        }

        CaptureComment userCaptureComment = capture.getCommentForUserId(userAccountId);
        if (userCaptureComment != null) {
            userComment = userCaptureComment.getComment();
        }

        capturePercent = capture.getRatingPercentForId(userAccountId);

        PrettyTime p = new PrettyTime();
        captureTimeLocation = p.format(capture.getCreatedAtDate());

        // TODO: Add Location once location we find capture with location...
        String location = "";
        captureTimeLocation += location;

        ImageLoaderUtil.loadImageIntoView(mContext, profileImageUrl, viewHolder.profileImage2);

        final String finalUserAccountId = userAccountId;
        viewHolder.profileImage2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionsHandler.launchUserProfile(finalUserAccountId);
                    }
                }
        );

        viewHolder.userName.setText(userName);
        if (userComment != "") {
            viewHolder.userComment.setText(userComment);
            viewHolder.userComment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.userComment.setText("");
            viewHolder.userComment.setVisibility(View.GONE);
        }
        if (capturePercent > 0.0f) {
            viewHolder.userCaptureRatingBar.setVisibility(View.VISIBLE);
            viewHolder.userCaptureRatingBar.setPercent(capturePercent);
        } else {
            viewHolder.userCaptureRatingBar.setVisibility(View.GONE);
        }

        viewHolder.captureTimeLocation.setText(captureTimeLocation);
    }

    private void setupParticipantsRatingsAndComments(FeedViewHolder viewHolder,
            CaptureDetails capture) {
        viewHolder.participantsCommentsRatingsContainer.removeAllViewsInLayout();
        CommentRatingRowView.LayoutParams layoutParams = new CommentRatingRowView.LayoutParams(
                CommentRatingRowView.LayoutParams.MATCH_PARENT,
                CommentRatingRowView.LayoutParams.WRAP_CONTENT);
        int verticalSpacing = mContext.getResources()
                .getDimensionPixelSize(R.dimen.cap_feed_row_small_vertical_spacing);

        // TODO: Finalize Test out with feed with participants.
        Account capturingAccount = capture.getCapturerParticipant();
        ArrayList<Account> participants = capture.getCommentingParticipants();
        int numDisplayedComments = 0;
        if (participants != null) {
            viewHolder.participantsCommentsRatingsContainer.setVisibility(View.VISIBLE);
            for (Account participant : participants) {
                // Skip comments by the user who captured, otherwise it will show as duplicate
                if (capturingAccount.getId().equalsIgnoreCase(participant.getId())) {
                    continue;
                }
                CaptureComment comment = capture.getCommentForUserId(participant.getId());
                String commentText = comment != null ? comment.getComment() : "";
                float rating = capture.getRatingPercentForId(participant.getId());
                if (commentText != "" || rating > 0.0f) {
                    CommentRatingRowView commentRow = new CommentRatingRowView(mContext);
                    commentRow.setPadding(0, verticalSpacing, 0, verticalSpacing);
                    commentRow.setNameCommentWithRating(participant.getFullName(), commentText,
                            rating);
                    viewHolder.participantsCommentsRatingsContainer
                            .addView(commentRow, layoutParams);
                    numDisplayedComments++;
                }
            }
        }
        if (numDisplayedComments == 0) {
            viewHolder.participantsCommentsRatingsContainer.setVisibility(View.GONE);
        }
    }

    private void setupActionButtonStates(FeedViewHolder viewHolder, final CaptureDetails capture) {
        int numLikes = capture.getLikesCount();
        String likesCountText = mContext.getString(R.string.cap_feed_likes_count, numLikes);
        viewHolder.likesCount.setText(likesCountText);
        boolean userLikesCapture = capture.doesUserLikeCapture(UserInfo.getUserId(mContext));

        viewHolder.likeButton.setSelected(userLikesCapture);

        viewHolder.rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.rateAndCommentForCapture(capture);
            }
        });

        viewHolder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsHandler.writeCommentForCapture(capture);
            }
        });

        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, String.valueOf(capture.getLikingParticipants()));
                mActionsHandler.toggleLikeForCapture(capture);
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

    public static interface FeedItemActionsHandler {

        public void writeCommentForCapture(CaptureDetails capture);

        public void rateAndCommentForCapture(CaptureDetails capture);

        public void toggleLikeForCapture(CaptureDetails capture);

        public void launchWineProfile(WineProfile wineProfile);

        public void launchUserProfile(String userAccountId);

        public void launchTaggedUserListing(CaptureDetails capture);
    }

    static class FeedViewHolder {

        ImageView wineImage;

        TextView producerName;

        TextView wineName;

        View taggedParticipantsContainer;

        CircleImageView profileImage1;

        ArrayList<CircleImageView> taggedParticipantImages;

        ImageView moreTaggedParticipantsButton;

        CircleImageView profileImage2;

        TextView userName;

        TextView userComment;

        TextView captureTimeLocation;

        RatingsBarView userCaptureRatingBar;

        LinearLayout participantsCommentsRatingsContainer;

        View rateButton;

        View commentButton;

        View likeButton;

        TextView likesCount;

        View menuButton;
    }
}
