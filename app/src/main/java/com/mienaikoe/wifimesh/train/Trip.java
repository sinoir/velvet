package com.mienaikoe.wifimesh.train;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesse on 1/31/2015.
 */
public class Trip {


    private Map<TrainStation, Date>  stationTimings = new HashMap<TrainStation, Date>();
    private TrainLine line;
    private TrainDirection direction;


    public Trip(TrainLine line, TrainDirection direction){
        this.line = line;
        this.direction = direction;
    }

    public void addStationTiming(TrainStation station, long timing){
        this.stationTimings.put(station, new Date(timing*1000));
    }



    public Map<TrainStation, Date> getStationTimings() {
        return stationTimings;
    }

    public TrainLine getLine() {
        return line;
    }

    public TrainDirection getDirection() {
        return direction;
    }
}
