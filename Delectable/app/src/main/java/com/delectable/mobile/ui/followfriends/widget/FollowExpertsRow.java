package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FollowExpertsRow extends RelativeLayout {

    @InjectView(R.id.profile_image)
    CircleImageView mProfileImage;

    @InjectView(R.id.name)
    FontTextView mName;

    @InjectView(R.id.bio)
    FontTextView mBio;

    @InjectView(R.id.follow_button)
    ImageButton mFollowButton;

    private ActionsHandler mActionsHandler;

    public FollowExpertsRow(Context context) {
        this(context, null);
    }

    public FollowExpertsRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowExpertsRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_find_experts, this);
        ButterKnife.inject(this);
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void updateData(String profileImageUrl, String name, String bio, boolean isFollowing) {
        ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mProfileImage);
        mName.setText(name);
        mBio.setText(bio);
        mFollowButton.setSelected(isFollowing);
    }

    @OnClick(R.id.follow_button)
    protected void onFollowButtonClick(ImageButton v) {
        v.setSelected(!v.isSelected());
        if (mActionsHandler != null) {
            mActionsHandler.toggleFollow(v.isSelected());
        }
    }


    public static interface ActionsHandler {

        //TODO need to return expert object here in addition the the following boolean
        public void toggleFollow(boolean isFollowing);
    }

}
