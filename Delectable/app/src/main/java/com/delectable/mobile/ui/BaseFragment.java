package com.delectable.mobile.ui;

import com.delectable.mobile.ui.common.dialog.ConfirmationDialog;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment implements LifecycleProvider {

    @Inject
    EventBus mEventBus;

    private State state;

    private Set<LifecycleListener> lifecycleListeners;

    private ActionBar mActionBar;

    private ImageView mCustomHomeImageView;

    private RelativeLayout mCustomActionBarView;

    private boolean mIsUsingCustomActionbarView = false;

    public BaseFragment() {
        lifecycleListeners = new CopyOnWriteArraySet<LifecycleListener>();
        state = State.constructed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = State.created;
        mActionBar = getActivity().getActionBar();
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

    public void showConfirmation(String title, String message, String positiveText,
            int requestCode) {
        ConfirmationDialog dialog = ConfirmationDialog
                .newInstance(title, message, positiveText, this, requestCode);
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
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
