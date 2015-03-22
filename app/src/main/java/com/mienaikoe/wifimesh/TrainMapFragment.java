package com.mienaikoe.wifimesh;


import com.mienaikoe.wifimesh.map.SubwayMapView;
import com.mienaikoe.wifimesh.map.VectorInstruction;
import com.mienaikoe.wifimesh.map.VectorMapIngestor;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Jesse on 1/18/2015.
 */
public class TrainMapFragment extends BaseFragment {

    private Context context;

    private ViewGroup rootView;
    private TrainStation station;
    private SubwayMapView subwayMapView;
    private SubwayMapView catchupMap;

    private TrainSystem system;
    private VectorMapIngestor mapIngestor;
    private List<VectorInstruction> lines;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        this.context = inflater.getContext();

        this.subwayMapView = new SubwayMapView(this.context);
        this.subwayMapView.setSystem( system );
        this.subwayMapView.setIngestor(mapIngestor);
        if( catchupMap != null ){
            this.subwayMapView.catchup(catchupMap);
            this.catchupMap = null; // free memory
        }

        this.rootView.addView(this.subwayMapView);

        return this.rootView;
    }



    public void setSystem( TrainSystem system ){
        this.system = system;
    }

    public void setMapIngestor(VectorMapIngestor mapIngestor) {
        this.mapIngestor = mapIngestor;
    }


    public void onStart(){
        super.onStart();
        this.mEventBus.register(this.subwayMapView);
    }

    public void onStop(){
        super.onStop();
        this.mEventBus.unregister(this.subwayMapView);
    }

    public void onEventMainThread( StationSelectEvent event ){
        if (event == null) {
            return;
        }
        this.station = event.getStation();
    }

    public void catchupFrom( TrainMapFragment other ){
        this.setSystem(other.system);
        this.catchupMap = other.subwayMapView;
    }

    public void toggleStreets(){
        this.subwayMapView.toggleStreets();
    }




}
