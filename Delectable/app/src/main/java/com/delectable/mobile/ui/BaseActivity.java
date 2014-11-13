package com.delectable.mobile.ui;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.util.CrashlyticsUtil;
import com.kahuna.sdk.KahunaAnalytics;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = this.getClass().getSimpleName();

    private Uri mDeepLinkUriData;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private Location mLastLocation;

    /**
     * Track fragments that have been attached to the activity, so that we can easily forward out
     * onActivityResult() messages to attached, visible fragments.
     */
    private List<WeakReference<Fragment>> mFragmentList = new ArrayList<WeakReference<Fragment>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Deep Link stuff
        Intent intent = getIntent();
        // Action not used yet, not sure if we'll need it.  Right now the action only VIEW.
        if (intent != null) {
            String action = intent.getAction();
            mDeepLinkUriData = intent.getData();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        CrashlyticsUtil.log(TAG + ".onStart");
        mGoogleApiClient.connect();
        KahunaAnalytics.start();
    }

    @Override
    protected void onStop() {
        CrashlyticsUtil.log(TAG + ".onStop");
        mGoogleApiClient.disconnect();
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

    public void replaceWithFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fade_in, R.anim.fade_out,
                R.anim.fade_in, R.anim.fade_out);

        //replace() and addToBackStack() need to use the same tag name, or else we won't be able to retrieve
        //the fragment from the backstack in onActivityResult
        String fragmentName = fragment.getClass().getSimpleName();
        transaction.replace(R.id.container, fragment, fragmentName);
        transaction.addToBackStack(fragmentName);

        transaction.commit();
    }

    // Replaces Fragment completely with no backstack
    public void popAndReplaceWithFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fade_in, R.anim.fade_out,
                R.anim.fade_in, R.anim.fade_out);

        //replace() and addToBackStack() need to use the same tag name, or else we won't be able to retrieve
        //the fragment from the backstack in onActivityResult
        String fragmentName = fragment.getClass().getSimpleName();
        transaction.replace(R.id.container, fragment, fragmentName);
        transaction.commit();
    }

    public String getDeepLinkParam(String key) {
        String param = null;
        if (isFromDeepLink()) {
            param = mDeepLinkUriData.getQueryParameter(key);
        }
        return param;
    }

    public boolean isFromDeepLink() {
        return mDeepLinkUriData != null;
    }

    public Uri getDeepLinkUriData() {
        return mDeepLinkUriData;
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    /**
     * Update Last location by pinging the LocationService once.
     */
    public void updateLastLocation() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // We only want 1 update
        mLocationRequest.setNumUpdates(1);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // We just want to update once, and remove the listener
                        mLastLocation = location;
                        Log.d(TAG, "Updated Location:" + location);
                    }
                });
    }

    //region Google Play Services Callbacks
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "On LocationServices Connected: " + bundle);
        updateLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO : Properly handle different error scenarios, i.e: Google Play Services is missing.
        Log.e(TAG, "On LocationServices Connection Failed: " + connectionResult);
    }
    //endregion

}
