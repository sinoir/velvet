package com.mienaikoe.wifimesh.train;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jesse on 1/21/2015.
 */
public class TrainStop {

    private TrainStation station;
    private Set<TrainLine> lines = new HashSet<TrainLine>();

    private double longitude = 0;
    private double latitude = 0;

    public TrainStop(TrainStation station, double latitude, double longitude){
        this.station = station;
        this.latitude = latitude;
        this.longitude = longitude;
        this.station.addStop(this);
    }

    public void addLine(TrainLine line){
        this.lines.add(line);
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Set<TrainLine> getLines() {
        return lines;
    }

    public TrainStation getStation() {
        return station;
    }
}
