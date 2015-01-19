package com.mienaikoe.wifimesh.train;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainStation {

    private String name;
    private Set<TrainLine> lines = new HashSet<TrainLine>();

    private float longitude = 0;
    private float latitude = 0;


    public TrainStation(String name){
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void addLine(TrainLine line){
        this.lines.add(line);
    }

    public Set<TrainLine> getLines() {
        return lines;
    }

    public float distance(float longitude, float latitude){
        return (float)Math.sqrt(Math.pow(this.longitude - longitude, 2) + Math.pow(this.latitude + latitude, 2));
    }

    public boolean equals(TrainStation other){
        return this.name.equals(other.getName());
    }





    private static final Set<TrainStation> all = new HashSet<TrainStation>();

    public static TrainStation closestStation(float longitude, float latitude){
        float closestRadius = 0;
        TrainStation closestStation = null;
        for( TrainStation station : all ) {
            float radius = station.distance(longitude, latitude);
            if( radius < closestRadius ){
                closestRadius = radius;
                closestStation = station;
            }
        }
        return closestStation;
    }
}
