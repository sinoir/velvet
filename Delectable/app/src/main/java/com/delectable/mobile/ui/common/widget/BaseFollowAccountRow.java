package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BaseFollowAccountRow extends RelativeLayout {

    @InjectView(R.id.profile_image)
    protected CircleImageView mProfileImage;

    @InjectView(R.id.name)
    protected FontTextView mName;

    @InjectView(R.id.subtext)
    protected FontTextView mInfluencerTitles;

    @InjectView(R.id.follow_button)
    protected ImageButton mFollowButton;

    public BaseFollowAccountRow(Context context) {
        this(context, null);
    }

    public BaseFollowAccountRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseFollowAccountRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_follow_account, this);
        ButterKnife.inject(this);
    }

    protected void updateData(String profileImageUrl, String name, String influencerTitles,
            boolean isFollowing) {
        ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mProfileImage);
        mName.setText(name);
        mInfluencerTitles.setText(influencerTitles);
        mFollowButton.setSelected(isFollowing);
    }

    @OnClick(R.id.follow_button)
    protected void onFollowButtonClick(ImageButton v) {
        v.setSelected(!v.isSelected());
    }
}