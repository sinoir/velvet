package com.delectable.mobile.ui.wineprofile.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.wineprofile.fragment.RateCaptureFragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class RateCaptureActivity extends BaseActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = RateCaptureActivity.class.getSimpleName();

    private static final String PENDING_CAPTURE_ID = "PENDING_CAPTURE_ID";

    private String mPendingCaptureId;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;


    public static Intent newIntent(Context packageContext, String pendingCaptureId) {
        Intent intent = new Intent();
        intent.putExtra(PENDING_CAPTURE_ID, pendingCaptureId);
        intent.setClass(packageContext, RateCaptureActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mPendingCaptureId = args.getString(PENDING_CAPTURE_ID);
        }

        if (savedInstanceState == null) {
            RateCaptureFragment fragment = RateCaptureFragment.newInstance(mPendingCaptureId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
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
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    //region Google Play Services Callbacks
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "LocationServices.OnConnected");

        //Update Last location by pinging the LocationService once.
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // We only want 1 update
        locationRequest.setNumUpdates(1);

        //can take up to 5 seconds to for location to return
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, locationRequest, mLocationListener);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // We just want to update once, and remove the listener
            mLastLocation = location;
            Log.d(TAG, "Updated Location:" + location);
        }
    };

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "LocationServices.onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO : Properly handle different error scenarios, i.e: Google Play Services is missing.
        Log.e(TAG, "LocationServices.OnConnectionFailed: " + connectionResult);
    }
    //endregion
}
