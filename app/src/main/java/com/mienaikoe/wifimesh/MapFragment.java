package com.mienaikoe.wifimesh;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

/**
 * Created by Jesse on 1/18/2015.
 */
public class MapFragment extends Fragment {

    private Context context;
    private TrainStation station;

    private TypefaceTextView stationName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        this.context = inflater.getContext();

        this.stationName = (TypefaceTextView) rootView.findViewById(R.id.station_name);

        return rootView;
    }

    public void setStation( TrainStation station ){
        this.station = station;
        this.stationName.setText(station.getName());
    }




}
