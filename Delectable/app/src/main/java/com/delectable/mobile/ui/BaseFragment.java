package com.delectable.mobile.ui;

import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.cache.CaptureListingModel;
import com.delectable.mobile.api.cache.CaptureNoteListingModel;
import com.delectable.mobile.api.cache.CapturesPendingCapturesListingModel;
import com.delectable.mobile.api.cache.PaymentMethodModel;
import com.delectable.mobile.api.cache.PendingCapturesModel;
import com.delectable.mobile.api.cache.ServerInfo;
import com.delectable.mobile.api.cache.ShippingAddressModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.ui.camera.activity.WineCaptureActivity;
import com.delectable.mobile.ui.common.dialog.ConfirmationNoTitleDialog;
import com.delectable.mobile.ui.events.NavigationDrawerCloseEvent;
import com.delectable.mobile.ui.registration.activity.LoginActivity;
import com.delectable.mobile.util.AnalyticsUtil;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.delectable.mobile.util.KahunaUtil;
import com.facebook.Session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment implements LifecycleProvider, HideableActionBar {

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    protected CapturesPendingCapturesListingModel mCapturesPendingCapturesListingModel;

    @Inject
    protected PendingCapturesModel mPendingCapturesModel;

    @Inject
    protected CaptureDetailsModel mCaptureDetailsModel;

    @Inject
    protected ShippingAddressModel mShippingAddressModel;

    @Inject
    protected PaymentMethodModel mPaymentMethodModel;

    @Inject
    protected EventBus mEventBus;

    @Inject
    protected AnalyticsUtil mAnalytics;

    private Set<LifecycleListener> lifecycleListeners;

    private Toolbar mActionBarToolbar;

    public BaseFragment() {
        lifecycleListeners = new CopyOnWriteArraySet<LifecycleListener>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashlyticsUtil.log(TAG + ".onCreate");
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        CrashlyticsUtil.log(TAG + ".onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        CrashlyticsUtil.log(TAG + ".onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        CrashlyticsUtil.log(TAG + ".onStart");
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onStart();
        }
        try {
            mEventBus.register(this);
        } catch (NullPointerException e) {
            Log.e(TAG, "EventBus is null. Missing injection?");
        } catch (Throwable t) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CrashlyticsUtil.log(TAG + ".onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        CrashlyticsUtil.log(TAG + ".onPause");
        hideKeyboard();
    }

    @Override
    public void onStop() {
        super.onStop();
        CrashlyticsUtil.log(TAG + ".onStop");
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onStop();
        }
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CrashlyticsUtil.log(TAG + ".onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CrashlyticsUtil.log(TAG + ".onDestroy");
        lifecycleListeners.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        CrashlyticsUtil.log(TAG + ".onDetach");
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
        activity.replaceWithFragment(fragment, true);
    }

    public void replaceWithNewFragment(BaseFragment fragment) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.replaceWithFragment(fragment, false);
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

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    /**
     * Call this in Fragment#onActivityCreated
     */
    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = getBaseActivity().getActionBarToolbar();
        }
        return mActionBarToolbar;
    }

    /**
     * Call this in Fragment#onActivityCreated
     */
    protected ActionBar getActionBar() {
        // Toolbar will be set as ActionBar when calling getActionBarToolbar()
        if (mActionBarToolbar == null) {
            getActionBarToolbar();
        }
        return getBaseActivity().getSupportActionBar();
    }

    /**
     * Call this in Fragment#onActivityCreated
     */
    protected void setActionBarTitle(SpannableString title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title);
        }
    }

    /**
     * Call this in Fragment#onActivityCreated
     */
    protected void setActionBarTitle(String title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title);
        }
    }

    /**
     * Call this in Fragment#onActivityCreated
     */
    protected void setActionBarSubtitle(SpannableString subtitle) {
        if (getActionBar() != null) {
            getActionBar().setSubtitle(subtitle);
        }
    }

    /**
     * Call this in Fragment#onActivityCreated
     */
    protected void setActionBarSubtitle(String subtitle) {
        if (getActionBar() != null) {
            getActionBar().setSubtitle(subtitle);
        }
    }

    /**
     * Call this in Fragment#onActivityCreated
     */
    protected void enableBackButton(boolean enable) {
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(enable);
        } else {
            Log.d(TAG, "Cannot enable back button, ActionBar is null");
        }
    }

    @Override
    public void showOrHideActionBar(boolean show) {
        getBaseActivity().showOrHideActionBar(show);
    }

    @Override
    public void showOrHideActionBar(boolean show, int delay) {
        getBaseActivity().showOrHideActionBar(show, delay);
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

        //Clear listings
        mCapturesPendingCapturesListingModel.clear();
        mPendingCapturesModel.clear();
        mCaptureDetailsModel.clear();

        // Clear some caches
        mShippingAddressModel.clear();
        mPaymentMethodModel.clear();

        CaptureNoteListingModel.clear();
        CaptureListingModel.clear();

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

    public void closeNavigationDrawer() {
        mEventBus.post(new NavigationDrawerCloseEvent());
    }

    public void launchWineCapture() {
        Intent launchIntent = new Intent(getActivity(), WineCaptureActivity.class);
        startActivity(launchIntent);
    }

}
