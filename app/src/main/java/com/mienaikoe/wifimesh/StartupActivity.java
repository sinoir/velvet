package com.mienaikoe.wifimesh;


import android.content.Intent;
import android.location.Location;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mienaikoe.wifimesh.mesh.TestMeshActivity;
import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;


public class StartupActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ImageView mapView;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private LineFragment lineFragment;
    private MapFragment mapFragment;

    private TrainSystem trainSystem;

    private GoogleApiClient googleApiClient;

    private TypefaceTextView stationName;
    private GridLayout linesTiming;

    private TrainStation currentStation;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startService(new Intent(this, VelvetService.class)); Not now, mabe eventually
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

        this.stationName = (TypefaceTextView) findViewById(R.id.station_name);
        this.linesTiming = (GridLayout) findViewById(R.id.lines_timing);

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
                    mapFragment.setSystem(trainSystem);
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

        this.deferStation();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(this.getClass().getSimpleName(), "Location Changed: "+location.getLatitude() + ", " + location.getLongitude());
        Toast.makeText(this.getApplicationContext(), "Location Changed", Toast.LENGTH_LONG).show();
        if( location != null ) {
            TrainStation closestStation = trainSystem.closestStation(location.getLatitude(), location.getLongitude());
            this.setStation(closestStation);
        }
    }

    public void setStation(TrainStation station){
        LocationServices.FusedLocationApi.removeLocationUpdates( this.googleApiClient, this );
        this.stationName.setText(station.getName());
        this.linesTiming.removeAllViews();
        for( TrainLine line : station.getLines() ){
            TrainLineIcon icon = new TrainLineIcon( getApplicationContext(), line,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics())
            );

            this.linesTiming.addView(icon);

            TypefaceTextView timing = new TypefaceTextView( getApplicationContext() );
            timing.setCustomFont(getApplicationContext(), "fonts/HelveticaNeue-Medium.otf");
            timing.setText("Fake Timing");
            timing.setGravity(Gravity.CENTER_VERTICAL);
            timing.setTextColor(getResources().getColor(R.color.white));
            timing.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            timing.setPadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics())
            );
            this.linesTiming.addView(timing);
        }
        this.linesTiming.invalidate();

        mapFragment.setStation(station);
        lineFragment.setStation(station);
    }

    public void deferStation(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000*60); // walking for 1 minute will change enough with accuracy differences
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setFastestInterval(1000 * 10); // walking for 10 seconds won't get you far

        LocationServices.FusedLocationApi.requestLocationUpdates( this.googleApiClient, locationRequest, this);
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
