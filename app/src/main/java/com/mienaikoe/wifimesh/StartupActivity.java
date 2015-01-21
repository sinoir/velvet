package com.mienaikoe.wifimesh;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mienaikoe.wifimesh.mesh.TestMeshActivity;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import java.security.Provider;


public class StartupActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ImageView mapView;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private LineFragment lineFragment;
    private MapFragment mapFragment;

    private TrainSystem trainSystem;
    private GoogleApiClient googleApiClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, VelvetService.class));
        setContentView(R.layout.activity_startup_activity);

        this.trainSystem = new TrainSystem(
                this.getApplicationContext().getResources().openRawResource(R.raw.stops_normalized),
                this.getApplicationContext().getResources().openRawResource(R.raw.lines_normalized),
                this.getApplicationContext().getResources().openRawResource(R.raw.transfers_normalized)
        );

        // Instantiate a ViewPager and a PagerAdapter.
        this.pager = (ViewPager) findViewById(R.id.pager);
        this.pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        this.pager.setAdapter(this.pagerAdapter);

        initLocationSystem();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_test_mesh:
                startActivity(new Intent(getApplicationContext(), TestMeshActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }







    @Override
    public void onBackPressed() {
        if (this.pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
        }
    }



    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    mapFragment = new MapFragment();
                    return mapFragment;
                case 1:
                    lineFragment = new LineFragment();
                    lineFragment.setTrainSystem(trainSystem);
                    return lineFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



    private void initLocationSystem(){
        this.googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        this.googleApiClient.connect();

    }


    @Override
    public void onConnected(Bundle connectionHint) {
        if (mapFragment != null && lineFragment != null) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
            this.onLocationChanged(lastLocation);
        }

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000*60*60);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setFastestInterval(1000*10);
        LocationServices.FusedLocationApi.requestLocationUpdates( this.googleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(this.getClass().getSimpleName(), "Location Changed");
        Toast.makeText(this.getApplicationContext(), "Location Changed", Toast.LENGTH_LONG).show();
        if( location != null ) {
            TrainStation closestStation = trainSystem.closestStation(location.getLatitude(), location.getLongitude());
            mapFragment.setStation(closestStation);
            lineFragment.setStation(closestStation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(this.getClass().getSimpleName(), "Connection to Play Services Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(this.getClass().getSimpleName(), "Connection to Play Services Failed");
    }

}
