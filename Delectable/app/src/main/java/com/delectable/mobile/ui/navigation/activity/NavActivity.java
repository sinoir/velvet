package com.delectable.mobile.ui.navigation.activity;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.events.NavigationEvent;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowFriendsFragment;
import com.delectable.mobile.ui.home.fragment.HomeFragment;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.ui.search.fragment.SearchFragment;
import com.delectable.mobile.ui.settings.fragment.SettingsFragment;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class NavActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = NavActivity.class.getSimpleName();

    @Inject
    EventBus mEventBus;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        setContentView(R.layout.activity_nav);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment
                .setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
        }
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

    public void onEventMainThread(NavigationEvent event) {
        onNavigationDrawerItemSelected(event.itemPosition);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        BaseFragment fragment = null;
        switch (position) {
            case NavHeader.NAV_HOME:
                fragment = new HomeFragment();
                break;
            case NavHeader.NAV_FIND_FRIENDS:
                fragment = new FollowFriendsFragment();
                break;
            case NavHeader.NAV_SEARCH:
                fragment = new SearchFragment();
                break;
            case NavHeader.NAV_SETTINGS:
                fragment = new SettingsFragment();
                break;
        }
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
}
