package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.CircleImageView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileHeaderMainView extends LinearLayout {

    private TextView mUserNameTextView;

    private TextView mCaptureWineCountTextView;

    private TextView mFollowerCountTextView;

    private TextView mFollowingCountTextView;

    private CircleImageView mUserImageView;

    private ProfileHeaderMainViewActionListeners mActionListener;

    public ProfileHeaderMainView(Context context) {
        this(context, null);
    }

    public ProfileHeaderMainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileHeaderMainView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.profile_header_main, this);
        setOrientation(VERTICAL);

        mUserImageView = (CircleImageView) findViewById(R.id.profile_image1);
        mUserNameTextView = (TextView) findViewById(R.id.user_name);
        mCaptureWineCountTextView = (TextView) findViewById(R.id.capture_wine_count);
        mFollowerCountTextView = (TextView) findViewById(R.id.followers_count);
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

    public void setWineCount(int wineCount) {
        String wineCountText = getResources()
                .getQuantityString(R.plurals.wine_count, wineCount, wineCount);
        mCaptureWineCountTextView.setText(wineCountText);
    }

    public void setFollowerCount(int followerCount) {
        String followerCountText = getResources().getQuantityString(R.plurals.followers_count,
                followerCount, followerCount);
        mFollowerCountTextView.setText(followerCountText);
    }

    public void setFollowingCount(int followingCount) {
        String followingCountText = getResources().getString(R.string.following_count,
                followingCount);
        mFollowingCountTextView.setText(followingCountText);
    }

    public TextView getUserNameTextView() {
        return mUserNameTextView;
    }

    public TextView getFollowerCountTextView() {
        return mFollowerCountTextView;
    }

    public TextView getFollowingCountTextView() {
        return mFollowingCountTextView;
    }

    public ProfileHeaderMainViewActionListeners getActionListener() {
        return mActionListener;
    }

    public void setActionListener(
            ProfileHeaderMainViewActionListeners actionListener) {
        mActionListener = actionListener;
    }

    // TODO: Implement clicker for follow/following text views
    public static interface ProfileHeaderMainViewActionListeners {

        public void followerCountClicked();

        public void followingCountClicked();
    }
}
