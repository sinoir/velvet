package com.delectable.mobile.ui.camera.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.camera.fragment.WineCaptureCameraFragment;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class WineCaptureActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = WineCaptureActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setContentView(R.layout.activity_toolbar_fragment_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WineCaptureCameraFragment())
                    .commit();
        }
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
                .requestLocationUpdates(mGoogleApiClient, locationRequest, LocationListener);
    }

    private LocationListener LocationListener = new LocationListener() {
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
