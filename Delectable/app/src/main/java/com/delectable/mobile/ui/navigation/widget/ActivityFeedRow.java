package com.delectable.mobile.ui.navigation.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.ActivityFeedItem;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.util.DateHelperUtil;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ActivityFeedRow extends RelativeLayout {

    private static final String TAG = ActivityFeedRow.class.getSimpleName();

    @InjectView(R.id.left_image)
    protected CircleImageView mLeftImage;

    @InjectView(R.id.text)
    protected TextView mText;

    @InjectView(R.id.time_ago)
    protected TextView mTimeAgo;

    @InjectView(R.id.right_image)
    protected ImageView mRightImage;

    private ActivityActionsHandler mActionsHandler;

    public ActivityFeedRow(Context context) {
        this(context, null);
    }

    public ActivityFeedRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivityFeedRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_activity_feed, this);
        ButterKnife.inject(this);
    }

    public void updateData(ActivityFeedItem data) {
        String activityTime = DateHelperUtil.getPrettyTimePastOnly(data.getCreatedAtDate());

        mText.setText(data.getText());
        mTimeAgo.setText(activityTime);

        // Setup Left and Right Images
        if (data.getLeftImageLink() != null && data.getLeftImageLink().getPhoto() != null) {
            ImageLoaderUtil
                    .loadImageIntoView(getContext(), data.getLeftImageLink().getPhoto().getUrl(),
                            mLeftImage);
        } else {
            //show delectable avatar if left image doesn't exist
            ImageLoaderUtil
                    .loadImageIntoView(getContext(), R.drawable.delectable_avatar, mLeftImage);
        }

        if (data.getRightImageLink() != null && data.getRightImageLink().getPhoto() != null) {
            mRightImage.setVisibility(View.VISIBLE);
            ImageLoaderUtil
                    .loadImageIntoView(getContext(),
                            data.getRightImageLink().getPhoto().getSmallest(), mRightImage);
        } else {
            mRightImage.setVisibility(View.GONE);
            mRightImage.setImageDrawable(null);
        }

        // Setup Left and Right Image Links

        if (data.getLeftImageLink() != null && data.getLeftImageLink().getUrl() != null) {
            final String leftImageUrl = data.getLeftImageLink().getUrl();
            mLeftImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionsHandler != null) {
                        mActionsHandler.openDeepLink(leftImageUrl);
                    }
                }
            });
        } else {
            mLeftImage.setOnClickListener(null);
        }

        if (data.getRightImageLink() != null && data.getRightImageLink().getUrl() != null) {
            final String rightImageUrl = data.getRightImageLink().getUrl();
            mRightImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionsHandler != null) {
                        mActionsHandler.openDeepLink(rightImageUrl);
                    }
                }
            });
        } else {
            mRightImage.setOnClickListener(null);
        }
    }

    public void setActionsHandler(ActivityActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public static interface ActivityActionsHandler {

        public void openDeepLink(String url);

    }
}
