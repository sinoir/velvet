package com.delectable.mobile.ui;

import com.delectable.mobile.data.ServerInfo;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.common.dialog.ConfirmationNoTitleDialog;
import com.delectable.mobile.ui.registration.activity.LoginActivity;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.facebook.Session;
import com.iainconnor.objectcache.CacheManager;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment implements LifecycleProvider {

    @Inject
    EventBus mEventBus;

    @Inject
    CacheManager mCache;

    private State state;

    private Set<LifecycleListener> lifecycleListeners;

    private ActionBar mActionBar;

    private ImageView mCustomHomeImageView;

    private RelativeLayout mCustomActionBarView;

    private boolean mIsUsingCustomActionbarView = false;

    private boolean mHasCustomActionBarTitle;

    public BaseFragment() {
        lifecycleListeners = new CopyOnWriteArraySet<LifecycleListener>();
        state = State.constructed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = State.created;
        mActionBar = getActivity().getActionBar();
        setHasOptionsMenu(true);
    }

    /**
     * subclasses should call super on this method in order to ensure that the actionbar doesn't
     * have any menu item artifacts from other fragemnts.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        state = State.started;
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onStart();
        }
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        state = State.paused;
        hideKeyboard();
    }

    @Override
    public void onResume() {
        super.onResume();
        state = State.resumed;
        toggleCustomActionBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        state = State.stopped;
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onStop();
        }
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        state = State.destroyed;
        lifecycleListeners.clear();
        if (mHasCustomActionBarTitle) {
            getActivity().getActionBar().setTitle(null);
        }
    }

    @Override
    public void registerLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
    }

    @Override
    public void unregister(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    public void launchNextFragment(BaseFragment fragment) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.replaceWithFragment(fragment);
    }

    public void showToastError(String error) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }

    public void showToastError(int stringResId) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), stringResId, Toast.LENGTH_LONG).show();
        }
    }

    public void showConfirmationNoTitle(String message, String positiveText, String negativeText,
            int requestCode) {
        ConfirmationNoTitleDialog dialog = ConfirmationNoTitleDialog
                .newInstance(message, positiveText, negativeText);
        dialog.setTargetFragment(this, requestCode);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    protected void setActionBarTitle(String title) {
        mHasCustomActionBarTitle = true;
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle(title);
        }
    }

    public void signout() {
        // Close FB Session
        Session session = Session.getActiveSession();
        if (session != null) {
            session.closeAndClearTokenInformation();
        }

        // Clear User Data
        UserInfo.onSignOut(getActivity());
        ServerInfo.onSignOut(getActivity());
        CrashlyticsUtil.onSignOut();

        try {
            // TODO: run this on background thread?  Also, might be handy to be selective on what gets deleted.
            mCache.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Launch Sign Up / Log In screen
        Intent launchIntent = new Intent();
        launchIntent.setClass(getActivity(), LoginActivity.class);
        startActivity(launchIntent);
        getActivity().finish();
        KahunaUtil.trackLogOut();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            view.clearFocus();
        }
    }

    /**
     * * Override home icon with custom view with click listener <p/> Note: Having a title will push
     * this view to the right of the title.
     *
     * @param resId    - Drawable resource
     * @param listener (Optional) - OnClick for the custom view
     */
    public void overrideHomeIcon(int resId, View.OnClickListener listener) {
        // TODO: Find if there's a better alternative: having a title will push this custom view to the right
        ActionBar.LayoutParams customViewpParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams imageViewpParams = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        mCustomActionBarView = new RelativeLayout(getActivity());

        // Custom Image View
        mCustomHomeImageView = new ImageView(getActivity());
        int horizontalPadding = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, metrics);
        mCustomHomeImageView.setPadding(horizontalPadding, 0, horizontalPadding, 0);
        mCustomHomeImageView.setImageResource(resId);

        if (listener != null) {
            mCustomHomeImageView.setOnClickListener(listener);
        }

        mCustomActionBarView.addView(mCustomHomeImageView, imageViewpParams);

        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setCustomView(mCustomActionBarView, customViewpParams);
        mIsUsingCustomActionbarView = true;
    }

    private void toggleCustomActionBar() {
        if (mActionBar == null) {
            return;
        }
        if (mIsUsingCustomActionbarView && mCustomActionBarView != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setCustomView(mCustomActionBarView);
        } else if (mActionBar.getCustomView() != null) {
            mActionBar.setDisplayShowCustomEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private static enum State {
        constructed,
        created,
        started,
        resumed,
        paused,
        stopped,
        destroyed
    }

}
