package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.Capture;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
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

    public FollowFeedAdapter(Activity context, ArrayList<CaptureDetails> data) {
        mContext = context;
        mData = data;
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

    // TODO: Possibly use Expandable List view or something, must display bottom comments / ratings
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

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (FeedViewHolder) rowView.getTag();
        }
        setupTopWineDetails(viewHolder, capture);
        setupTaggedParticipants(viewHolder, capture);
        setupUserCommentsRating(viewHolder, capture);

        setupParticipantsRatingsAndComments(viewHolder, capture);

        return rowView;
    }

    private void setupTopWineDetails(FeedViewHolder viewHolder, CaptureDetails capture) {
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
    }

    private void setupTaggedParticipants(FeedViewHolder viewHolder, CaptureDetails capture) {
        String profileImageUrl = getThumbnailParticipantPhotoFromAccount(
                capture.getCapturerParticipant());

        // TODO: Combine data from other participants objects
        ArrayList<Account> taggedParticipants = capture.getRegisteredParticipants() != null
                ? capture.getRegisteredParticipants() : new ArrayList<Account>();

        boolean hasCaptureParticipants = taggedParticipants.size() > 0;

        ImageLoaderUtil.loadImageIntoView(mContext, profileImageUrl, viewHolder.profileImage1);

        if (hasCaptureParticipants) {
            viewHolder.taggedParticipantsContainer.setVisibility(View.VISIBLE);
        } else {
            viewHolder.taggedParticipantsContainer.setVisibility(View.GONE);
        }

        for (int i = 0; i < viewHolder.taggedParticipantImages.size(); i++) {
            if (taggedParticipants.size() > i) {
                String imageUrl = getThumbnailParticipantPhotoFromAccount(
                        taggedParticipants.get(i));
                // TODO: Add Touchstate to open User Profile
                ImageLoaderUtil.loadImageIntoView(mContext, imageUrl,
                        viewHolder.taggedParticipantImages.get(i));
                viewHolder.taggedParticipantImages.get(i).setVisibility(View.VISIBLE);
            } else {
                viewHolder.taggedParticipantImages.get(i).setVisibility(View.INVISIBLE);
            }
        }

        if (taggedParticipants.size() == 3) {
            // TODO: Add Touchstate to Open more tagged profile listing
            viewHolder.moreTaggedParticipantsButton.setVisibility(View.VISIBLE);
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

        // TODO: Add Location once location is added to model
        String location = "";
        captureTimeLocation += location;

        ImageLoaderUtil.loadImageIntoView(mContext, profileImageUrl, viewHolder.profileImage2);
        viewHolder.userName.setText(userName);
        if (userComment != "") {
            viewHolder.userComment.setText(userComment);
            viewHolder.userComment.setVisibility(View.VISIBLE);
        } else {
            viewHolder.userComment.setVisibility(View.GONE);
        }
        viewHolder.userCaptureRatingBar.setPercent(capturePercent);

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
        ArrayList<Account> participants = capture.getCapturerParticipants();
        if (participants != null) {
            for (Account participant : participants) {
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
                }
            }
        }
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
    }
}
