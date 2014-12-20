package com.delectable.mobile.ui;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.kahuna.sdk.KahunaAnalytics;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends ActionBarActivity
        implements HideableActionBar {

    protected static final int ACTIONBAR_HIDE_ANIM_DURATION = 300;

    protected static final int ACTIONBAR_SHOW_ANIM_DURATION = 200;

    private final String TAG = this.getClass().getSimpleName();

    private Toolbar mActionBarToolbar;

    private boolean mActionBarShown = true;

    private List<View> mHeaderViews = new ArrayList<View>();

    /**
     * Track fragments that have been attached to the activity, so that we can easily forward out
     * onActivityResult() messages to attached, visible fragments.
     */
    private List<WeakReference<Fragment>> mFragmentList = new ArrayList<WeakReference<Fragment>>();

    @Override
    protected void onStart() {
        super.onStart();
        CrashlyticsUtil.log(TAG + ".onStart");
        KahunaAnalytics.start();
    }

    @Override
    protected void onStop() {
        CrashlyticsUtil.log(TAG + ".onStop");
        KahunaAnalytics.stop();
        super.onStop();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        mFragmentList.add(new WeakReference<Fragment>(fragment));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //shoot onActivityResult out to attached and visible fragment
        for (WeakReference<Fragment> ref : mFragmentList) {
            Fragment fragment = ref.get();
            if (fragment != null && fragment.isVisible()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        /*
        alternative implementation that uses backstacks. Can't use this because NavActivity doesn't
        maintain a backstack for the rootview screens

        //grab fragment on top of the backstack and invoke it's onActivityResult to pass information back to fragment
        //platform oddity, activities don't forward onActivityResult back to current fragment
        int lastPosition = getFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(
                lastPosition);
        Fragment fragment = getFragmentManager().findFragmentByTag(entry.getName());
        fragment.onActivityResult(requestCode, resultCode, data);
        */
    }

    public void setSystemUiVisibility(int flags) {
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    public void showOrHideStatusBar(boolean show) {
        // make status bar translucent on v19+ and hide if requested
        int flags = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ? (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | (show
                ? 0 : View.SYSTEM_UI_FLAG_FULLSCREEN))
                : View.SYSTEM_UI_FLAG_VISIBLE;
        setSystemUiVisibility(flags);
    }

    @Override
    public void showOrHideActionBar(boolean show) {
        showOrHideActionBar(show, 0);
    }

    @Override
    public void showOrHideActionBar(final boolean show, final int delay) {
        if (show == mActionBarShown) {
            return;
        }
        if (delay > 0) {
            mActionBarToolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mActionBarShown = show;
                    onActionBarShowOrHide(show);
                }
            }, delay);
        } else {
            mActionBarShown = show;
            onActionBarShowOrHide(show);
        }
    }

    public void onActionBarShowOrHide(boolean shown) {
        Toolbar toolbar = getActionBarToolbar();
        Interpolator interpolator = new AccelerateDecelerateInterpolator();

        if (shown) {
            toolbar.animate()
                    .setDuration(ACTIONBAR_SHOW_ANIM_DURATION)
                    .translationY(0)
//                    .alpha(1)
                    .setInterpolator(interpolator);
        } else {
            toolbar.animate()
                    .setDuration(ACTIONBAR_HIDE_ANIM_DURATION)
                    .translationY(-toolbar.getHeight())
//                    .alpha(0)
                    .setInterpolator(interpolator);
        }

        // translate header views (e.g. tab strip) so they take up the space of the hidden action bar
        for (View view : mHeaderViews) {
            if (shown) {
                view.animate()
                        .translationY(0)
                        .setDuration(ACTIONBAR_SHOW_ANIM_DURATION)
                        .setInterpolator(interpolator);
            } else {
                view.animate()
                        .translationY(-toolbar.getHeight())
                        .setDuration(ACTIONBAR_HIDE_ANIM_DURATION)
                        .setInterpolator(interpolator);
            }
        }
    }

    public Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    @Override
    public ActionBar getSupportActionBar() {
        if (mActionBarToolbar == null) {
            getActionBarToolbar();
        }
        return super.getSupportActionBar();
    }

    public void registerHeaderView(View headerView) {
        if (!mHeaderViews.contains(headerView)) {
            mHeaderViews.add(headerView);
        }
    }

    public void deregisterHeaderView(View headerView) {
        if (mHeaderViews.contains(headerView)) {
            mHeaderViews.remove(headerView);
        }
    }

    /**
     * Should be called in place of finish() when the "Up" button is pressed from a deep linked
     * Activity
     */
    public void finishDeepLinkActivity() {
        Intent launchNavIntent = new Intent();
        launchNavIntent.setClass(getApplicationContext(), NavActivity.class);
        launchNavIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(launchNavIntent);
        super.finish();
    }

    protected void launchUserProfile(boolean clearBackStack) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID,
                UserInfo.getUserId(App.getInstance()));
        intent.setClass(this, UserProfileActivity.class);
        if (clearBackStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        startActivity(intent);
        if (clearBackStack) {
            finish();
        }
    }

    public void replaceWithFragment(BaseFragment fragment, boolean addToBackstack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        //replace() and addToBackStack() need to use the same tag name, or else we won't be able to retrieve
        //the fragment from the backstack in onActivityResult
        String fragmentName = fragment.getClass().getSimpleName();
        transaction.replace(R.id.container, fragment, fragmentName);
        if (addToBackstack) {
            transaction.addToBackStack(fragmentName);
        }

        transaction.commit();
    }
}
