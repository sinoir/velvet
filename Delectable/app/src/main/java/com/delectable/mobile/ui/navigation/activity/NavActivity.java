package com.delectable.mobile.ui.navigation.activity;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.events.ui.InsetsChangedEvent;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.followfriends.fragment.FollowFriendsFragment;
import com.delectable.mobile.ui.home.fragment.HomeFragment;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.ui.profile.fragment.YourWinesFragment;
import com.delectable.mobile.ui.search.activity.SearchActivity;
import com.delectable.mobile.ui.settings.fragment.SettingsFragment;
import com.delectable.mobile.util.AnalyticsUtil;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class NavActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = NavActivity.class.getSimpleName();

    @Inject
    protected EventBus mEventBus;

    @Inject
    AnalyticsUtil mAnalytics;

    private DrawerLayout mDrawerLayout;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private int mCurrentSelectedNavItem = NavHeader.NAV_DISCOVER;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle = null;

    private TextView mToolbarTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        App.injectMembers(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment.setUp(mDrawerLayout);

        mToolbarTitleView = (TextView) findViewById(R.id.toolbar_title);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        InsetsChangedEvent insetsEvent = mEventBus.getStickyEvent(InsetsChangedEvent.class);
        if (insetsEvent != null) {
            onApplyWindowInsets(insetsEvent.insets);
        }
        restoreActionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnalytics.flush();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mCurrentSelectedNavItem = position;
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseFragment fragment = null;
        switch (position) {
//            case NavHeader.NAV_PROFILE:
            //fragment = UserProfileFragment.newInstance(UserInfo.getUserId(this));
            //mTitle = "";
            //mCurrentSelectedNavItem = NavHeader.NAV_PROFILE;
            // TODO always launch user profiles as fragments to allow access to nav drawer?
            // launching as new activity from within NavigationDrawerFragment to prevent double action bar issue
//                return;
            //break;
            case NavHeader.NAV_DISCOVER:
                fragment = new HomeFragment();
                mTitle = getResources().getString(R.string.app_name);
                mTitle = null;
                break;
            case NavHeader.NAV_YOUR_WINES:
                fragment = new YourWinesFragment(); // extends UserProfileF
//                startActivity(
//                        UserProfileActivity.newIntent(this, UserInfo.getUserId(App.getInstance())));
                mTitle = getResources().getString(R.string.navigation_your_wines);
                break;
            case NavHeader.NAV_FIND_FRIENDS:
                fragment = new FollowFriendsFragment();
                mTitle = getResources().getString(R.string.navigation_find_friends);
                break;
            case NavHeader.NAV_SETTINGS:
                fragment = new SettingsFragment();
                mTitle = getResources().getString(R.string.settings_title);
                break;
            default:
                mCurrentSelectedNavItem = NavHeader.NAV_DISCOVER;
        }
        if (fragment != null) {
            restoreActionBar();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setTitle((String) null);
//        actionBar.setLogo(R.drawable.feed_logo);
        if (mTitle != null) {
            mToolbarTitleView.setText(mTitle);
//            actionBar.setTitle(mTitle);
//            actionBar.setDisplayShowTitleEnabled(true);
//            actionBar.setDisplayUseLogoEnabled(false);
        } else {
//            actionBar.setDisplayShowTitleEnabled(false);
            mToolbarTitleView.setText(getResources().getString(R.string.app_name));
//            actionBar.setTitle((String) null);
//            actionBar.setDisplayUseLogoEnabled(true);
        }
        actionBar.setSubtitle(null);
    }

    @Override
    public void onBackPressed() {
        if (NavHeader.NAV_DISCOVER == mCurrentSelectedNavItem) {
            super.onBackPressed();
        } else {
            // navigate to home fragment
            mEventBus.post(new NavigationEvent(NavHeader.NAV_DISCOVER));
        }
    }

    public void onEventMainThread(InsetsChangedEvent event) {
        onApplyWindowInsets(event.insets);
    }

    private void onApplyWindowInsets(Rect insets) {
        if (insets == null) {
            return;
        }
//        // adjust toolbar padding when status bar is translucent
//        mToolbar.setPadding(0, insets.top, 0, 0);
//        mToolbarContrast.setPadding(0, insets.top, 0, 0);
    }

}
