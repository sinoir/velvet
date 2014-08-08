package com.delectable.mobile.ui.navigation.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.camera.activity.WineCaptureActivity;
import com.delectable.mobile.ui.home.fragment.HomeFragment;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.delectable.mobile.ui.settings.fragment.SettingsFragment;
import com.delectable.mobile.ui.navigation.widget.NavHeader;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class NavActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private ImageView mCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nav);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Setup Floating Camera Button
        mCameraButton = (ImageView) findViewById(R.id.camera_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWineCapture();
            }
        });
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
        FragmentManager fragmentManager = getFragmentManager();
        BaseFragment fragment = null;
        switch (position) {
            case NavHeader.NAV_HOME:
                fragment = new HomeFragment();
                break;
            case NavHeader.NAV_FIND_FRIENDS:
                // TODO: Find People Screen
                Toast.makeText(this, "Show Find People", Toast.LENGTH_SHORT).show();
                break;
            case NavHeader.NAV_SEARCH:
                // TODO: Search Screen
                Toast.makeText(this, "Show Search", Toast.LENGTH_SHORT).show();
                break;
            case NavHeader.NAV_SETTINGS:
                fragment = new SettingsFragment();
                break;
        }
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    public void launchWineCapture() {
        Intent launchIntent = new Intent();
        launchIntent.setClass(this, WineCaptureActivity.class);
        startActivity(launchIntent);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
}
