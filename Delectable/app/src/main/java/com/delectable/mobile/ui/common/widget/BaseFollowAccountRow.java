package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
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
            int relationship) {
        //set no_photo first, so that when the user flicks through the list, it doesn't show another account's picture
        ImageLoaderUtil.loadImageIntoView(getContext(), R.drawable.no_photo, mProfileImage);
        ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mProfileImage);
        mName.setText(name);
        if (influencerTitles == null || influencerTitles.trim().equalsIgnoreCase("")) {
            mInfluencerTitles.setVisibility(View.GONE);
        } else {
            mInfluencerTitles.setVisibility(View.VISIBLE);
            mInfluencerTitles.setText(influencerTitles);
        }

        //set state of follow button depending on relationship
        if (relationship == AccountMinimal.RELATION_TYPE_SELF) {
            mFollowButton.setVisibility(View.INVISIBLE);
        }
        if (relationship == AccountMinimal.RELATION_TYPE_NONE) {
            mFollowButton.setVisibility(View.VISIBLE);
            mFollowButton.setSelected(false);
        }
        if (relationship == AccountMinimal.RELATION_TYPE_FOLLOWING) {
            mFollowButton.setVisibility(View.VISIBLE);
            mFollowButton.setSelected(true);
        }
    }

    @OnClick(R.id.follow_button)
    protected void onFollowButtonClick(ImageButton v) {
        v.setSelected(!v.isSelected());
    }
}
