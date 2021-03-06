package com.mienaikoe.wifimesh;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.mienaikoe.wifimesh.map.VectorMapIngestor;
import com.mienaikoe.wifimesh.sinoir.GtfsUpdateService;
import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;



public class StartupActivity extends BaseActivity
        implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_LINES = 1;


    private TrainMapFragment trainMapFragment;
    private MapFragment googleMapFragment;
    private TrainSystem trainSystem;
    private GoogleApiClient googleApiClient;
    private boolean googleApiConnected = false;
    private TypefaceTextView stationName;
    private GridLayout linesTiming;
    private LatLng currentLocation;

    private boolean usingGoogleMap = false;
    private TrainStation currentStation;
    private VectorMapIngestor ingestor;

    private MapName mCurrentMap = MapName.DAY;

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startup_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawMap(MapName.DAY);
        startService(new Intent(this, GtfsUpdateService.class));



        if (savedInstanceState == null) {
            this.initMaps();
        }

        this.stationName = (TypefaceTextView) findViewById(R.id.station_name);
        this.linesTiming = (GridLayout) findViewById(R.id.lines_timing);

        initLocationSystem();
    }

    private void drawMap(MapName map) {
        mCurrentMap = map;
        // Fill Out Train System Information
        if( this.trainSystem == null ) {
            this.trainSystem = new TrainSystem(
                    this.getApplicationContext().getResources().openRawResource(R.raw.stops_normalized),
                    this.getApplicationContext().getResources().openRawResource(R.raw.lines_normalized),
                    this.getApplicationContext().getResources()
                            .openRawResource(R.raw.transfers_normalized),
                    this.getApplicationContext().getResources().openRawResource(R.raw.subway_entrances)
            );
            TrainSystemModel.setTrainSystem(trainSystem);
        }

        this.ingestor = new VectorMapIngestor(getApplicationContext(), map.toString());
        this.trainSystem.fillRectangles(ingestor);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, GtfsUpdateService.class));
        super.onDestroy();
    }


    private static final String[] ASYNC_URLS = new String[]{
            "http://sinoir-appifyed.rhcloud.com/mta/subway/update/1",
            "http://sinoir-appifyed.rhcloud.com/mta/subway/update/2"
    };


    @Override
    public void onResume() {
        super.onResume();

        mEventBus.register(trainSystem);
        StationSelectEvent event = (StationSelectEvent)mEventBus.getStickyEvent(StationSelectEvent.class);
        if( event != null ){
            mEventBus.postSticky(event); // repost sticky for unresumed
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mEventBus.unregister(trainSystem);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_service_toggle);
        menuItem.setIcon(mCurrentMap.getIcon());
        return         super.onPrepareOptionsMenu(menu);

    }

    private static enum MapName {
        DAY("final_map.svg", R.drawable.ic_sun),
        NIGHT("nightmap.svg", R.drawable.ic_action_image_brightness_3),
        WEEKEND("weekendmap.svg", R.drawable.ic_action_notification_mms);

        private String mFilename;

        private int mIcon;

        private MapName(String filename, int icon) {
            mFilename = filename;
            mIcon = icon;
        }

        public int getIcon() {
            return mIcon;
        }

        @Override
        public String toString() {
            return mFilename;
        }

        public MapName getNext() {
            if (this==DAY) {
                return NIGHT;
            }
            if (this == NIGHT) {
                return WEEKEND;
            }
            if (this == WEEKEND) {
                return DAY;
            }
            return DAY;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_service_toggle:
                MapName newMap = mCurrentMap.getNext();
                this.drawMap(newMap);
                invalidateOptionsMenu();

                TrainMapFragment oldMap = trainMapFragment;
                trainMapFragment = new TrainMapFragment();
                trainMapFragment.setMapIngestor(ingestor);
                trainMapFragment.catchupFrom(oldMap);

                getFragmentManager().beginTransaction()
                        .remove(oldMap)
                        .add(R.id.container, trainMapFragment)
                        .hide(googleMapFragment)
                        .show(trainMapFragment)
                        .commit();
                return true;
            case R.id.action_locate:
                this.updateLocation();
                return true;
            case R.id.action_map_toggle:
                this.toggleMap();
                return true;
            case R.id.action_streets_toggle:
                this.trainMapFragment.toggleStreets();
                return true;
            case R.id.action_trainlines:
                mEventBus.postSticky(new TrainLinesFragment.InitEvent(currentStation));
                startActivity( new Intent(getApplicationContext(), LineActivity.class) );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleMap() {
        if( this.usingGoogleMap ){
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .hide(googleMapFragment)
                    .show(trainMapFragment)
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .hide(trainMapFragment)
                    .show(googleMapFragment)
                    .commit();
        }
        this.usingGoogleMap = !this.usingGoogleMap;
    }


    private void initMaps(){
        trainMapFragment = new TrainMapFragment();
        trainMapFragment.setSystem(trainSystem);
        trainMapFragment.setMapIngestor(ingestor);
        googleMapFragment = new MapFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.container, trainMapFragment)
                .add(R.id.container, googleMapFragment)
                .hide(googleMapFragment)
                .commit();
    }



    //endregion Lifecycle



    public void onEventMainThread( StationSelectEvent event ){
        if (event == null) {
            return;
        }
        setStation(event.getStation());
    }





    private void initLocationSystem(){
        this.googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        this.googleApiClient.connect();
    }

    private void updateLocation() {
        if( this.googleApiConnected ){
            this.onConnected(null); // update with immediately known location
        }
        Log.i(this.getClass().getSimpleName(), "Updating Location");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // very quick update if possible
        locationRequest.setFastestInterval(1); // very quick update if possible
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates( this.googleApiClient, locationRequest, this);
    }





    @Override
    public void onConnected(Bundle connectionHint) {
        this.googleApiConnected = true;
        if (trainMapFragment != null) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
            this.onLocationChanged(lastLocation);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.i(this.getClass().getSimpleName(),
                    "Location Changed: " + location.getLatitude() + ", " + location.getLongitude());
            this.currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            TrainStation closestStation = trainSystem.closestStation(this.currentLocation);
            this.setStation(closestStation);
            LocationServices.FusedLocationApi.removeLocationUpdates( this.googleApiClient, this );
        } else {
            Log.w(this.getClass().getSimpleName(), "Location was Null");
        }
    }

    public void setStation(TrainStation station) {

        //LocationServices.FusedLocationApi.removeLocationUpdates( this.googleApiClient, this );
        this.currentStation = station;

        this.stationName.setText(station.getName());
        this.linesTiming.removeAllViews();
        for( TrainLine line : station.getLines() ){
            renderStationLine(station, line);
        }

        this.linesTiming.invalidate();

        googleMapFragment.getMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMapFragment.getMap().setBuildingsEnabled(false);
                googleMapFragment.getMap().setTrafficEnabled(false);
                googleMapFragment.getMap().setMyLocationEnabled(true);
                for( LatLng entrance : currentStation.getEntrances() ) {
                    MarkerOptions mo = new MarkerOptions();
                    mo.position(entrance);
                    googleMapFragment.getMap().addMarker(mo);
                }
                googleMapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(currentStation.getCenter(), 16));
            }
        });
    }


    private ViewGroup renderStationLine(TrainStation station, TrainLine line){
        RelativeLayout topLine = new RelativeLayout(this.getApplicationContext());
        this.linesTiming.addView(topLine);
        GridLayout.LayoutParams linesLayout = (GridLayout.LayoutParams)topLine.getLayoutParams();
        linesLayout.width = 420;
        topLine.setLayoutParams(linesLayout);

        int largeIconSize = getResources().getDimensionPixelSize(R.dimen.train_icon_large);
        TrainLineIcon icon = new TrainLineIcon(this);
        icon.setTrainLine(line, largeIconSize);

                topLine.addView(icon);
        RelativeLayout.LayoutParams iconParams = (RelativeLayout.LayoutParams) icon
                .getLayoutParams();
        iconParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
        icon.setLayoutParams(iconParams);

        GridLayout timingGrid = new GridLayout(this.getApplicationContext());
        topLine.addView(timingGrid);
        RelativeLayout.LayoutParams timingParams = (RelativeLayout.LayoutParams) timingGrid
                .getLayoutParams();
        timingParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
        timingGrid.setLayoutParams(timingParams);

        int timingPaddingHoriz = (int) TypedValue.
                applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        int timingPaddingVert = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());

        Date[] timings = trainSystem.getTimings(station, line);

        TypefaceTextView timing = new TypefaceTextView( getApplicationContext() );
        timingGrid.addView( timing );

        timing.setTypeface(FontEnum.HELVETICA_NEUE_MEDIUM);

        if( timings[0] != null ) {
            long northDiff = TimeUnit.MINUTES.convert(timings[0].getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
            timing.setText(String.valueOf(northDiff) + "m");
        }
        timing.setTextColor(getResources().getColor(R.color.white));
        timing.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        timing.setPadding(timingPaddingHoriz, timingPaddingVert, timingPaddingHoriz,
                timingPaddingVert);

        TypefaceTextView timing2 = new TypefaceTextView(getApplicationContext());
        timingGrid.addView(timing2);
        timing.setTypeface(FontEnum.HELVETICA_NEUE_MEDIUM);

        if( timings[1] != null ) {
            long southDiff = TimeUnit.MINUTES.convert(timings[1].getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
            timing.setText(String.valueOf(southDiff) + "m");
        }
        timing2.setTextColor(getResources().getColor(R.color.white));
        timing2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        timing2.setPadding(timingPaddingHoriz, timingPaddingVert, timingPaddingHoriz, timingPaddingVert);

        return topLine;
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
