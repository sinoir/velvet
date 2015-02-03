package com.mienaikoe.wifimesh;


import com.mienaikoe.wifimesh.map.TrainView;
import com.mienaikoe.wifimesh.map.VectorInstruction;
import com.mienaikoe.wifimesh.map.VectorMapIngestor;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Jesse on 1/18/2015.
 */
public class TrainMapFragment extends BaseFragment {

    private Context context;

    private ViewGroup rootView;
    private TrainStation station;
    private TrainView trainView;

    private TrainSystem system;
    private VectorMapIngestor mapIngestor;
    private List<VectorInstruction> lines;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        this.context = inflater.getContext();

        this.trainView = new TrainView(this.context);
        this.trainView.setSystem( system );
        this.trainView.setIngestor(mapIngestor);

        this.rootView.addView(this.trainView);

        return this.rootView;
    }



    public void setSystem( TrainSystem system ){
        this.system = system;
    }

    public void setMapIngestor(VectorMapIngestor mapIngestor) {
        this.mapIngestor = mapIngestor;
    }

    public void setStation( TrainStation station ){
        if( station.hasRectangles() ) {
            this.trainView.setStation(station);
        }
        this.station = station;
    }

    public void toggleStreets(){
        this.trainView.toggleStreets();
    }




}
