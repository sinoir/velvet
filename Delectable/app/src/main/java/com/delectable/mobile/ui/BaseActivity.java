package com.delectable.mobile.ui;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.kahuna.sdk.KahunaAnalytics;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public abstract class BaseActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private boolean mIsFromDeepLink;

    private Uri mDeepLinkUriData;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Deep Link stuff
        Intent intent = getIntent();
        // Action not used yet, not sure if we'll need it.  Right now the action only VIEW.
        String action = intent.getAction();
        mDeepLinkUriData = intent.getData();

        if (mDeepLinkUriData != null) {
            mIsFromDeepLink = true;
        } else {
            mIsFromDeepLink = false;
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

        mGoogleApiClient.connect();
        KahunaAnalytics.start();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        KahunaAnalytics.stop();
        super.onStop();
    }

    /**
     * Should be called in place of finish() when the "Up" button is pressed from a deep linked
     * Activity
     */
    public void finishDeepLinkActivity() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfoList = manager.getRunningTasks(1);
        boolean isNavActivityInTask = taskInfoList.size() > 0 && taskInfoList.get(0).baseActivity
                .getShortClassName().contains(
                        "NavActivity");
        if (!isNavActivityInTask) {
            Intent launchIntent = new Intent();
            launchIntent.setClass(getApplicationContext(), NavActivity.class);
            startActivity(launchIntent);
        }
        super.finish();
    }

    public void replaceWithFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // TODO: Should we animate?
        transaction.setCustomAnimations(
                android.R.animator.fade_in, android.R.animator.fade_out,
                android.R.animator.fade_in, android.R.animator.fade_out);

        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public String getDeepLinkParam(String key) {
        String param = null;
        if (isFromDeepLink() && getDeepLinkUriData() != null) {
            param = mDeepLinkUriData.getQueryParameter(key);
        }
        return param;
    }

    public boolean isFromDeepLink() {
        return mIsFromDeepLink;
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
