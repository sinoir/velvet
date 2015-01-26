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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mienaikoe.wifimesh.map.TrainView;
import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

/**
 * Created by Jesse on 1/18/2015.
 */
public class MapFragment extends Fragment {

    private Context context;

    private ViewGroup rootView;
    private TrainStation station;
    private TrainView trainView;
    private TrainSystem system;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        this.context = inflater.getContext();

        this.trainView = new TrainView(this.context);
        this.trainView.setSystem( system );
        this.rootView.addView(this.trainView);

        return this.rootView;
    }

    public void setSystem( TrainSystem system ){
        this.system = system;
    }

    public void setStation( TrainStation station ){
        this.trainView.setCenter( station.getViewX(), station.getViewY() );

        this.station = station;
    }




}
