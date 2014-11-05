package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.CircleImageView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ProfileHeaderMainView extends LinearLayout {

    @InjectView(R.id.user_name)
    protected TextView mUserNameTextView;

    @InjectView(R.id.capture_wine_count)
    protected TextView mCaptureWineCountTextView;

    @InjectView(R.id.followers_count)
    protected TextView mFollowerCountTextView;

    @InjectView(R.id.following_count)
    protected TextView mFollowingCountTextView;

    @InjectView(R.id.profile_image1)
    protected CircleImageView mUserImageView;

    private ActionListener mActionListener;

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
        ButterKnife.inject(this);
        setOrientation(VERTICAL);
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

    public ActionListener getActionListener() {
        return mActionListener;
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    @OnClick(R.id.capture_wine_count)
    protected void onWineCountTextClick() {
        if (mActionListener != null) {
            mActionListener.wineCountClicked();
        }
    }

    @OnClick(R.id.followers_count)
    protected void onFollowersCountTextClick() {
        if (mActionListener != null) {
            mActionListener.followerCountClicked();
        }
    }

    @OnClick(R.id.following_count)
    protected void onFollowingCountTextClick() {
        if (mActionListener != null) {
            mActionListener.followingCountClicked();
        }
    }

    public static interface ActionListener {

        public void wineCountClicked();

        public void followerCountClicked();

        public void followingCountClicked();
    }
}
