package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.SimpleViewPagerAdapter;
import com.delectable.mobile.util.ImageLoaderUtil;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileHeaderView extends RelativeLayout implements
        ProfileHeaderMainView.ActionListener {

    public static final int STATE_FOLLOWING = 0;

    public static final int STATE_NOT_FOLLOWING = 1;

    public static final int STATE_SELF = 2;

    private final ArrayList<View> mPagerViews;

    private ProfileHeaderMainView mProfileHeaderMainView;

    private ProfileHeaderBioView mProfileHeaderBioView;

    private ProfileHeaderActionListener mActionListener;

    private TextView mFollowButton;

    private String mFollowText;

    private String mFollowingText;

    private ViewPager mViewPager;

    private CirclePageIndicator mIndicator;

    private SimpleViewPagerAdapter mAdapter;

    public ProfileHeaderView(Context context) {
        this(context, null);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.profile_header, this, true);

        mViewPager = (ViewPager) findViewById(R.id.profile_header_viewpager);
        mIndicator = (CirclePageIndicator) findViewById(R.id.pager_indicator);

        mProfileHeaderMainView = new ProfileHeaderMainView(context);
        mProfileHeaderMainView.setActionListener(this);
        mProfileHeaderBioView = new ProfileHeaderBioView(context);

        // Setup ViewPager Adapter for Main/Bio
        mPagerViews = new ArrayList<View>();
        mPagerViews.add(mProfileHeaderMainView);

        mAdapter = new SimpleViewPagerAdapter(mPagerViews);
        mViewPager.setAdapter(mAdapter);
        // Must set pager to indicator after pager has adapter
        mIndicator.setViewPager(mViewPager);
        // Default is gone, unless we add bio view
        mIndicator.setVisibility(View.GONE);

        mFollowText = context.getString(R.string.profile_follow);
        mFollowingText = context.getString(R.string.profile_following_cap);

        mFollowButton = (TextView) findViewById(R.id.follow_button);

        // Setup FollowButton Click Listener
        mFollowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected()); //inverse selection

                boolean shouldFollow = v.isSelected();
                int followingState = shouldFollow ? STATE_FOLLOWING : STATE_NOT_FOLLOWING;
                setFollowingState(followingState);
                if (mActionListener != null) {
                    mActionListener.toggleFollowUserClicked(v.isSelected());
                }
            }
        });
    }

    private void addBioView() {
        // Add only if view hasn't been added
        if (!mPagerViews.contains(mProfileHeaderBioView)) {
            mPagerViews.add(mProfileHeaderBioView);
            mIndicator.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void removeBioView() {
        // Remove view if has already been added
        if (mPagerViews.contains(mProfileHeaderBioView)) {
            mPagerViews.remove(mProfileHeaderBioView);
            mIndicator.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    // The Fragment/Activity will handle populating the imageview with an image

    /**
     * Convenience method to set all views with AccountProfile data.
     */
    public void setDataToView(AccountProfile account) {
        String userName = account.getFullName();
        String imageUrl = account.getPhoto().getBestThumb();
        int numCaptures = account.getCaptureCount();
        boolean isInfluencer = account.isInfluencer();
        String influencerTitle = account.getInfluencerTitlesString();

        setWineCount(numCaptures);

        ImageLoaderUtil.loadImageIntoView(getContext(), imageUrl, getUserImageView());
        setUserName(userName);
        setInfluencer(isInfluencer, influencerTitle);
        setFollowerCount(account.getFollowerCount());
        setFollowingCount(account.getFollowingCount());
        setUserBio(account.getBio());

        if (account.isUserRelationshipTypeSelf()) {
            setFollowingState(STATE_SELF);
        } else if (account.isUserRelationshipTypeFollowing()) {
            setFollowingState(STATE_FOLLOWING);
        } else {
            setFollowingState(STATE_NOT_FOLLOWING);
        }
    }

    public CircleImageView getUserImageView() {
        return mProfileHeaderMainView.getUserImageView();
    }

    public void setUserName(String userName) {
        mProfileHeaderMainView.setUserName(userName);
    }

    public void setInfluencer(boolean isInfluencer, String influencerTitle) {
        mProfileHeaderMainView.setInfluencer(isInfluencer);
        // TODO influencer title in bio section
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
        if (userBio != null && !userBio.trim().equals("")) {
            addBioView();
        } else {
            removeBioView();
        }
        mProfileHeaderBioView.setUserBio(userBio);
    }

    public void setFollowingState(int state) {
        switch (state) {
            case STATE_FOLLOWING:
                updateButtonToUnfollow();
                shouldShowFollowButton(true);
                break;
            case STATE_NOT_FOLLOWING:
                updateButtonToFollowing();
                shouldShowFollowButton(true);
                break;
            case STATE_SELF:
                shouldShowFollowButton(false);
                break;
        }
    }

    private void updateButtonToFollowing() {
        mFollowButton.setText(mFollowText);
        mFollowButton.setSelected(false);
    }

    private void updateButtonToUnfollow() {
        mFollowButton.setText(mFollowingText);
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
    public void wineCountClicked() {
        if (mActionListener != null) {
            mActionListener.wineCountClicked();
        }
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

    public static interface ProfileHeaderActionListener extends ProfileHeaderMainView.ActionListener {

        public void toggleFollowUserClicked(boolean isFollowing);

    }
}
