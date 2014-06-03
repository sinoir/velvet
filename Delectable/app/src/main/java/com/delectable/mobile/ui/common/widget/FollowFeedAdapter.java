package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.Capture;
import com.delectable.mobile.api.models.CaptureDetails;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FollowFeedAdapter extends BaseAdapter {

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
            viewHolder.wineImage = (ImageView) rowView.findViewById(R.id.wine_image);
            viewHolder.producerName = (TextView) rowView.findViewById(R.id.producer_name);
            viewHolder.wineName = (TextView) rowView.findViewById(R.id.wine_name);
            viewHolder.profileImage1 = (CircleImageView) rowView.findViewById(R.id.profile_image1);
            viewHolder.withText = rowView.findViewById(R.id.with_text);
            viewHolder.participantsImageContainer = (RelativeLayout) rowView
                    .findViewById(R.id.participants_image_container);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (FeedViewHolder) rowView.getTag();
        }

        String wineImageUrl = "";
        String producerName = "";
        String wineName = "";
        String profileImageUrl = getThumbnailParticipantPhotoFromAccount(
                capture.getCapturerParticipant());

        boolean hasCaptureParticipants = (capture.getCapturerParticipants() != null
                && capture.getCapturerParticipants().size() > 0);

        if (capture.getWineProfile() != null) {
            producerName = capture.getWineProfile().getProducerName();
            wineName = capture.getWineProfile().getName();
            wineImageUrl = capture.getPhoto().getUrl();
        }

        Picasso.with(mContext).load(wineImageUrl).into(viewHolder.wineImage);
        Picasso.with(mContext).load(profileImageUrl)
                .into(viewHolder.profileImage1.getPicassoTarget());
        viewHolder.producerName.setText(producerName);
        viewHolder.wineName.setText(wineName);

        if (hasCaptureParticipants) {
            viewHolder.withText.setVisibility(View.VISIBLE);
            viewHolder.participantsImageContainer.setVisibility(View.VISIBLE);
        } else {
            viewHolder.withText.setVisibility(View.INVISIBLE);
            viewHolder.participantsImageContainer.setVisibility(View.GONE);
        }

        return rowView;
    }

    private void insertParticipantsImagesIntoView(RelativeLayout viewGroup,
            ArrayList<Account> participants) {
        viewGroup.removeAllViewsInLayout();
        // TODO: Build out mini icons for participants
        int startingId = 9000;
        for (Account account : participants) {
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

        CircleImageView profileImage1;

        View withText;

        RelativeLayout participantsImageContainer;
    }
}
