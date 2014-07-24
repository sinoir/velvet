package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.SimpleViewPagerAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ProfileHeaderView extends RelativeLayout implements
        ProfileHeaderMainView.ProfileHeaderMainViewActionListeners {

    public static final int STATE_FOLLOWING = 0;

    public static final int STATE_NOT_FOLLOWING = 1;

    public static final int STATE_SELF = 2;

    private ProfileHeaderMainView mProfileHeaderMainView;

    private ProfileHeaderBioView mProfileHeaderBioView;

    private ProfileHeaderActionListener mActionListener;

    private Button mFollowButton;

    private boolean mIsFollowing = false;

    private String mFollowText;

    private String mUnfollowText;

    private ViewPager mViewPager;

    private SimpleViewPagerAdapter mAdapter;

    public ProfileHeaderView(Context context) {
        this(context, null);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.profile_header, this);

        mViewPager = (ViewPager) findViewById(R.id.profile_header_viewpager);
        mProfileHeaderMainView = new ProfileHeaderMainView(context);
        mProfileHeaderBioView = new ProfileHeaderBioView(context);

        // Setup ViewPager Adapter for Main/Bio
        ArrayList<View> pagerViews = new ArrayList<View>();
        pagerViews.add(mProfileHeaderMainView);
        pagerViews.add(mProfileHeaderBioView);
        mAdapter = new SimpleViewPagerAdapter(pagerViews);
        mViewPager.setAdapter(mAdapter);

        mFollowText = context.getString(R.string.profile_follow);
        mUnfollowText = context.getString(R.string.profile_unfollow);

        mFollowButton = (Button) findViewById(R.id.follow_button);

        // Setup FollowButton Click Listener
        mFollowButton.setOnClickListener(new OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (mActionListener != null) {
                                                     mActionListener.toggleFollowUserClicked(
                                                             !mIsFollowing);
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

    public void setUserBio(String userBio) {
        mProfileHeaderBioView.setUserBio(userBio);
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

        public void toggleFollowUserClicked(boolean isFollowing);

        public void followerCountClicked();

        public void followingCountClicked();
    }
}
