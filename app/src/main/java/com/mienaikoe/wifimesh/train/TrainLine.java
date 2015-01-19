package com.mienaikoe.wifimesh.train;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainLine {

    private String name;
    private LinkedHashSet<TrainStation> stations = new LinkedHashSet<TrainStation>();

    public TrainLine(String name){
        this.name = name;
    }


    public String getName() {
        return name;
    }



    public LinkedHashSet<TrainStation> getStations() {
        return stations;
    }

    public void addStation(TrainStation station){
        this.stations.add(station);
        station.addLine(this);
    }
}
