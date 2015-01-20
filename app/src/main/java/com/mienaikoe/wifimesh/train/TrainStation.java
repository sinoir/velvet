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


    public TrainStation(String name, float latitude, float longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }


    public void addLine(TrainLine line, int id){
        if( !this.lines.contains(line.getName() )) {
            this.lines.add(line);
            line.addStation(this, id);
        }
    }

    public Set<TrainLine> getLines() {
        return lines;
    }

    public float distance(float latitude, float longitude){
        return (float)Math.sqrt(Math.pow(this.longitude - longitude, 2) + Math.pow(this.latitude - latitude, 2));
    }

    public boolean equals(TrainStation other){
        return this.name.equals(other.getName());
    }

    public TrainLine getRandomLine(){
        for( TrainLine line : this.lines ){
            return line;
        }
        return null;
    }

}
