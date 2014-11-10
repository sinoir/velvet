package com.delectable.mobile.ui.navigation.activity;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.followfriends.fragment.FollowFriendsFragment;
import com.delectable.mobile.ui.home.fragment.HomeFragment;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.ui.search.fragment.SearchFragment;
import com.delectable.mobile.ui.settings.fragment.SettingsFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class NavActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = NavActivity.class.getSimpleName();

    @Inject
    protected EventBus mEventBus;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private int mCurrentSelectedNavItem = NavHeader.NAV_HOME;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        App.injectMembers(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment
                .setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreActionBar();
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mCurrentSelectedNavItem = position;
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseFragment fragment = null;
        switch (position) {
            case NavHeader.NAV_PROFILE:
                //fragment = UserProfileFragment.newInstance(UserInfo.getUserId(this));
                //mTitle = "";
                //mCurrentSelectedNavItem = NavHeader.NAV_PROFILE;
                // TODO always launch user profiles as fragments to allow access to nav drawer?
                // launching as new activity from within NavigationDrawerFragment to prevent double action bar issue
                return;
            //break;
            case NavHeader.NAV_HOME:
                fragment = new HomeFragment();
                //mTitle = getResources().getString(R.string.app_name);
                mTitle = null;
                break;
            case NavHeader.NAV_FIND_FRIENDS:
                fragment = new FollowFriendsFragment();
                mTitle = getResources().getString(R.string.navigation_find_friends);
                break;
            case NavHeader.NAV_SEARCH:
                fragment = new SearchFragment();
                mTitle = getResources().getString(R.string.search_title);
                break;
            case NavHeader.NAV_SETTINGS:
                fragment = new SettingsFragment();
                mTitle = getResources().getString(R.string.settings_title);
                break;
            default:
                mCurrentSelectedNavItem = NavHeader.NAV_HOME;
        }
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setLogo(R.drawable.feed_logo);
        if (mTitle != null || (mTitle != null && mTitle.length() == 0)) {
            actionBar.setTitle(mTitle);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
        } else {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setTitle((String) null);
            actionBar.setDisplayUseLogoEnabled(true);
        }
        actionBar.setSubtitle(null);
    }

    @Override
    public void onBackPressed() {
        if (NavHeader.NAV_HOME == mCurrentSelectedNavItem) {
            super.onBackPressed();
        } else {
            // navigate to home fragment
            mEventBus.post(new NavigationEvent(NavHeader.NAV_HOME));
        }
    }
}
