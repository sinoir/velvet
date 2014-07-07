package com.delectable.mobile.ui.home.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.CircleImageView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavHeader extends RelativeLayout {

    private TextView mUserNameTextView;

    private TextView mFollowerCountTextView;

    private TextView mFollowingCountTextView;

    private CircleImageView mUserImageView;

    public NavHeader(Context context) {
        this(context, null);
    }

    public NavHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavHeader(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.navigation_header, this);

        mUserImageView = (CircleImageView) findViewById(R.id.profile_image1);
        mUserNameTextView = (TextView) findViewById(R.id.user_name);
        mFollowerCountTextView = (TextView) findViewById(R.id.followers_count);
        mFollowingCountTextView = (TextView) findViewById(R.id.following_count);
    }

    // The Fragment/Activity will handle populating the imageview with an image
    public CircleImageView getUserImageView() {
        return mUserImageView;
    }

    public void setUserName(String userName) {
        mUserNameTextView.setText(userName);
    }

    public void setFollowerCount(int followerCount) {
        String followerCountText = getResources()
                .getString(R.string.followers_count, followerCount);
        mFollowerCountTextView.setText(followerCountText);
    }

    public void setFollowingCount(int followingCount) {
        String followingCountText = getResources()
                .getString(R.string.followers_count, followingCount);
        mFollowingCountTextView.setText(followingCountText);
    }

}
