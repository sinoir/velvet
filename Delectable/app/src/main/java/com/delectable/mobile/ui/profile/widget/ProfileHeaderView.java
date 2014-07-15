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

    private ProfileHeaderMainView mProfileHeaderMainView;

    //TODO: ImageButtons?
    private Button mSwitchToListViewButton;

    private Button mSwitchToFeedViewButton;

    private ProfileHeaderActionListener mActionListener;

    public ProfileHeaderView(Context context) {
        this(context, null);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.profile_header, this);

        // TODO: Will be placing main header in a viewpager?
        mProfileHeaderMainView = (ProfileHeaderMainView) findViewById(R.id.profile_header_main);
        mSwitchToListViewButton = (Button) findViewById(R.id.switch_to_listing_button);
        mSwitchToFeedViewButton = (Button) findViewById(R.id.switch_to_feed_listing_button);
        mSwitchToListViewButton.setSelected(true);

        mSwitchToListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchToListViewButton.setSelected(true);
                mSwitchToFeedViewButton.setSelected(false);
                if (mActionListener != null) {
                    mActionListener.switchToListView();
                }
            }
        });
        mSwitchToFeedViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwitchToListViewButton.setSelected(false);
                mSwitchToFeedViewButton.setSelected(true);
                if (mActionListener != null) {
                    mActionListener.switchToFeedView();
                }
            }
        });
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

        public void switchToListView();

        public void switchToFeedView();

        public void wineCountClicked();

        public void findPeopleClicked();

        public void toggleFollowUserClicked();

        public void followerCountClicked();

        public void followingCountClicked();
    }
}
