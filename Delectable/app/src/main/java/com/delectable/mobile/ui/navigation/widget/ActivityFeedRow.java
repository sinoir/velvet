package com.delectable.mobile.ui.navigation.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.util.ImageLoaderUtil;

import org.ocpsoft.prettytime.PrettyTime;

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

    @InjectView(R.id.left_iamge)
    protected CircleImageView mLeftImage;

    @InjectView(R.id.text)
    protected TextView mText;

    @InjectView(R.id.time_ago)
    protected TextView mTimeAgo;

    @InjectView(R.id.right_image)
    protected ImageView mRightImage;


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

    public void updateData(ActivityRecipient data) {
        PrettyTime p = new PrettyTime();
        String activityTime = p.format(data.getCreatedAtDate());

        mText.setText(data.getText());
        mTimeAgo.setText(activityTime);

        if (data.getLeftImageLink() != null && data.getLeftImageLink().getPhoto() != null) {
            mLeftImage.setVisibility(View.VISIBLE);
            ImageLoaderUtil
                    .loadImageIntoView(getContext(), data.getLeftImageLink().getPhoto().getUrl(),
                            mLeftImage);
        } else {
            mLeftImage.setVisibility(View.GONE);
            mLeftImage.setImageDrawable(null);
        }

        if (data.getRightImageLink() != null && data.getRightImageLink().getPhoto() != null) {
            mRightImage.setVisibility(View.VISIBLE);
            ImageLoaderUtil
                    .loadImageIntoView(getContext(), data.getRightImageLink().getPhoto().getUrl(),
                            mRightImage);
        } else {
            mRightImage.setVisibility(View.GONE);
            mRightImage.setImageDrawable(null);
        }
        // TODO: Implement Click Listeners / Handler for Deep Links
    }
}
