package com.delectable.mobile.ui.navigation.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.CircleImageView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class NavHeader extends RelativeLayout {

    // These must reflec the order of the mNavigationItems and onNavigationItemClicked id orders
    public static final int NAV_PROFILE = -1;

    public static final int NAV_DISCOVER = 0;

    public static final int NAV_YOUR_WINES = 1;

    public static final int NAV_FIND_FRIENDS = 2;

    public static final int NAV_SETTINGS = 3;

    private static final String TAG = NavHeader.class.getSimpleName();

    @InjectView(R.id.profile_image1)
    CircleImageView mUserImageView;

    @InjectView(R.id.nav_user_name)
    TextView mUserNameTextView;

    @InjectView(R.id.wine_count)
    TextView mWineCountTextView;

    @InjectViews(
            {R.id.navigation_discover, R.id.navigation_your_wines, R.id.navigation_find_friends,
            R.id.navigation_settings})
    List<View> mNavigationItems;

    private NavHeaderActionListener mActionListener;

    private View mCurrentSelectedNav;

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
        ButterKnife.inject(this);
    }

    @OnClick({R.id.profile_image1, R.id.navigation_profile})
    void onProfileImageClicked() {
        if (mActionListener != null) {
            mActionListener.navHeaderUserImageClicked();
        }
    }

    @OnClick({R.id.navigation_discover, R.id.navigation_your_wines, R.id.navigation_find_friends,
            R.id.navigation_settings})
    void onNavigationClicked(View navItem) {
        toggleViewSelection(navItem);
        int navIndex = mNavigationItems.indexOf(navItem);
        Log.d(TAG, "Clicked Nav Item: " + navIndex);
        if (mActionListener != null) {
            mActionListener.navItemSelected(navIndex);
        }
    }

    /**
     * Helper to toggle selected view if it wasn't selected already, and deselect the last view
     *
     * @param selectedView - Current View to select
     * @return - Whether the current view was selected or not. This way we don't need to trigger a
     * click event
     */
    private boolean toggleViewSelection(View selectedView) {
        boolean wasSelected = false;
        if (mCurrentSelectedNav == null) {
            mCurrentSelectedNav = selectedView;
            selectedView.setActivated(true);
        } else if (!mCurrentSelectedNav.equals(selectedView)) {
            mCurrentSelectedNav.setActivated(false);
            selectedView.setActivated(true);
            mCurrentSelectedNav = selectedView;
        } else {
            wasSelected = true;
        }

        return wasSelected;
    }

    // The Fragment/Activity will handle populating the imageview with an image
    public CircleImageView getUserImageView() {
        return mUserImageView;
    }

    public void setUserName(String userName) {
        mUserNameTextView.setText(userName);
    }

    public void setWineCount(int wineCount) {
        String followerCountText = getResources().getQuantityString(R.plurals.wine_count,
                wineCount, wineCount);
        mWineCountTextView.setText(followerCountText);
    }

    public void setCurrentSelectedNavItem(int navItem) {
        if (navItem < 0) {
            mCurrentSelectedNav.setActivated(false);
        } else if (navItem < mNavigationItems.size()) {
            toggleViewSelection(mNavigationItems.get(navItem));
        }
    }

    public void setActionListener(NavHeaderActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface NavHeaderActionListener {

        public void navHeaderUserImageClicked();

        public void navItemSelected(int navItem);
    }
}
