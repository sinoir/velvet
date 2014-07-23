package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.CircleImageView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ProfileHeaderView extends RelativeLayout implements
        ProfileHeaderMainView.ProfileHeaderMainViewActionListeners {

    public static final int STATE_FOLLOWING = 0;

    public static final int STATE_NOT_FOLLOWING = 1;

    public static final int STATE_SELF = 2;

    private ProfileHeaderMainView mProfileHeaderMainView;

    private ProfileHeaderActionListener mActionListener;

    private Button mFollowButton;

    private boolean mIsFollowing = false;

    private String mFollowText;

    private String mUnfollowText;

    public ProfileHeaderView(Context context) {
        this(context, null);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.profile_header, this);

        mProfileHeaderMainView = (ProfileHeaderMainView) findViewById(R.id.profile_header_main);
        mFollowButton = (Button) findViewById(R.id.follow_button);

        mFollowText = context.getString(R.string.profile_follow);
        mUnfollowText = context.getString(R.string.profile_unfollow);

        mFollowButton.setOnClickListener(new OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (mActionListener != null) {
                                                     mActionListener.toggleFollowUserClicked();
                                                 }
                                                 if (mIsFollowing) {
                                                     setFollowingState(STATE_NOT_FOLLOWING);
                                                 } else {
                                                     setFollowingState(STATE_FOLLOWING);
                                                 }
                                             }
                                         }
        );
    }

    // The Fragment/Activity will handle populating the imageview with an image

    public CircleImageView getUserImageView() {
        return mProfileHeaderMainView.getUserImageView();
    }

    public void setUserName(String userName) {
        mProfileHeaderMainView.setUserName(userName);
    }

    public void setFollowerCount(int followerCount) {
        mProfileHeaderMainView.setFollowerCount(followerCount);
    }

    public void setFollowingCount(int followingCount) {
        mProfileHeaderMainView.setFollowingCount(followingCount);
    }

    public void setWineCount(int wineCount) {
        mProfileHeaderMainView.setWineCount(wineCount);
    }

    public void setFollowingState(int state) {
        switch (state) {
            case STATE_FOLLOWING:
                mIsFollowing = true;
                updateButtonToUnfollow();
                shouldShowFollowButton(true);
                break;
            case STATE_NOT_FOLLOWING:
                mIsFollowing = false;
                updateButtonToFollowing();
                shouldShowFollowButton(true);
                break;
            case STATE_SELF:
                mIsFollowing = false;
                shouldShowFollowButton(false);
                break;
        }
    }

    private void updateButtonToFollowing() {
        mFollowButton.setText(mFollowText);
        mFollowButton.setSelected(false);
    }

    private void updateButtonToUnfollow() {
        mFollowButton.setText(mUnfollowText);
        mFollowButton.setSelected(true);
    }

    public void shouldShowFollowButton(boolean showFollowButton) {
        if (showFollowButton) {
            mFollowButton.setVisibility(View.VISIBLE);
        } else {
            mFollowButton.setVisibility(View.GONE);
        }
    }

    public void setActionListener(ProfileHeaderActionListener actionListener) {
        mActionListener = actionListener;
    }

    @Override
    public void followerCountClicked() {
        if (mActionListener != null) {
            mActionListener.followerCountClicked();
        }
    }

    @Override
    public void followingCountClicked() {
        if (mActionListener != null) {
            mActionListener.followingCountClicked();
        }
    }

    public static interface ProfileHeaderActionListener {

        public void wineCountClicked();

        public void toggleFollowUserClicked();

        public void followerCountClicked();

        public void followingCountClicked();
    }
}
