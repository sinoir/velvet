package com.mienaikoe.wifimesh;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.transit.realtime.GtfsRealtime;

import com.mienaikoe.wifimesh.mesh.TestMeshActivity;
import com.mienaikoe.wifimesh.train.SinoirRestServiceTask;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;



public class StartupActivity extends BaseActivity
        implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_LINES = 1;

    private ImageView mapView;
    private MapFragment mapFragment;
    private TrainSystem trainSystem;
    private GoogleApiClient googleApiClient;
    private boolean googleApiConnected = false;
    private TypefaceTextView stationName;
    private GridLayout linesTiming;

    private TrainStation currentStation;

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startService(new Intent(this, TrainRealtimeService.class));
        setContentView(R.layout.activity_startup_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.trainSystem = new TrainSystem(
                this.getApplicationContext().getResources().openRawResource(R.raw.stops_normalized),
                this.getApplicationContext().getResources().openRawResource(R.raw.lines_normalized),
                this.getApplicationContext().getResources()
                        .openRawResource(R.raw.transfers_normalized),
                this.getApplicationContext().getResources().openRawResource(R.raw.subway_entrances),
                this.getApplicationContext().getResources().openRawResource(R.raw.vectors_stations)
        );
        TrainSystemModel.setTrainSystem(trainSystem);


        initTrainTiming();


        if (savedInstanceState == null) {
            mapFragment = new MapFragment();
            mapFragment.setSystem(trainSystem);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mapFragment)
                    .commit();
        }


        this.stationName = (TypefaceTextView) findViewById(R.id.station_name);
        this.linesTiming = (GridLayout) findViewById(R.id.lines_timing);

        initLocationSystem();
    }


    private static final String[] ASYNC_URLS = new String[]{
            "http://sinoir-appifyed.rhcloud.com/mta/subway/update/1",
            "http://sinoir-appifyed.rhcloud.com/mta/subway/update/2"
    };

    private void initTrainTiming() {
        SinoirRestServiceTask asyncTask = new SinoirRestServiceTask();
        asyncTask.execute(ASYNC_URLS);
        List<GtfsRealtime.FeedMessage> feedMessages = null;

        try {
            feedMessages = asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for( GtfsRealtime.FeedMessage feedMessage : feedMessages ) {
            this.trainSystem.fillTimings(feedMessage);
        }
    }

    @Override
    public void onResume() {
        // Get location and resume location updates
        //updateLocation(); // call update location only when requested
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_test_mesh:
                startActivity(new Intent(getApplicationContext(), TestMeshActivity.class));
                return true;
            case R.id.action_trainlines:
                Intent intent = new Intent(getApplicationContext(), LineActivity.class);
                startActivityForResult(intent, REQUEST_LINES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LINES:
                if (resultCode == Activity.RESULT_OK) {
                    StationSelectEvent event = mEventBus.getStickyEvent(StationSelectEvent.class);
                    if (event == null) {
                        return;
                    }
                    setStation(event.getStation());
                    //event is consumed, we can remove it now
                    mEventBus.removeStickyEvent(event);
                }
                return;
            default:
                return;
        }



    }
    //endregion Lifecycle






    private void initLocationSystem(){
        this.googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        this.googleApiClient.connect();
    }

    private void updateLocation() {
        if (this.googleApiConnected) {
            if (mapFragment != null) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
                this.onLocationChanged(lastLocation);
            }
        }
    }



    @Override
    public void onConnected(Bundle connectionHint) {
        this.googleApiConnected = true;
        updateLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.i(this.getClass().getSimpleName(),
                    "Location Changed: " + location.getLatitude() + ", " + location.getLongitude());

            TrainStation closestStation = trainSystem
                    .closestStation(new LatLng(location.getLatitude(), location.getLongitude()));

            this.setStation(closestStation);
        }
    }

    public void setStation(TrainStation station) {
        //LocationServices.FusedLocationApi.removeLocationUpdates( this.googleApiClient, this );
        this.stationName.setText(station.getName());
        this.linesTiming.removeAllViews();
        for( TrainLine line : station.getLines() ){
            renderStationLine(station, line);
        }
        this.linesTiming.invalidate();

        mapFragment.setStation(station);
    }



    private ViewGroup renderStationLine(TrainStation station, TrainLine line){
        LinearLayout layout = new LinearLayout(this.getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        this.linesTiming.addView(layout);

        RelativeLayout topLine = new RelativeLayout(this.getApplicationContext());
        layout.addView(topLine);

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

        int timingPaddingHoriz = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                getResources().getDisplayMetrics());
        int timingPaddingVert = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());

        Date[] timings = trainSystem.getTimings(station, line);

        TypefaceTextView timing = new TypefaceTextView( getApplicationContext() );
        timingGrid.addView( timing );

        timing.setTypeface(FontEnum.HELVETICA_NEUE_MEDIUM);

        if( timings[0] != null ) {
            long northDiff = TimeUnit.MINUTES.convert(timings[0].getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
            timing.setText(String.valueOf(northDiff) + "min");
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
            timing.setText(String.valueOf(southDiff) + "min");
        }

        timing2.setTextColor(getResources().getColor(R.color.white));
        timing2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        timing2.setPadding(timingPaddingHoriz, timingPaddingVert, timingPaddingHoriz, timingPaddingVert);


        return layout;
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
