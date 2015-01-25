package com.mienaikoe.wifimesh.train;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainStation {

    private String name;

    private Set<TrainStop> stops = new HashSet<TrainStop>();

    private double longitude = 0;
    private double latitude = 0;


    public TrainStation(String name){
        this.name = name;
    }


    public void addStop(TrainStop stop){
        this.stops.add(stop);

        double latitudeSum = 0;
        double longitudeSum = 0;
        for( TrainStop itStop : this.stops ){
            latitudeSum += itStop.getLatitude();
            longitudeSum += itStop.getLongitude();
        }
        this.latitude = latitudeSum / this.stops.size();
        this.longitude = longitudeSum / this.stops.size();
    }


    public String getName() {
        return name;
    }






    public Set<TrainLine> getLines() {
        SortedSet<TrainLine> allLines = new TreeSet<TrainLine>( new Comparator<TrainLine>(){
            @Override
            public int compare(TrainLine lhs, TrainLine rhs) {
                if( lhs == null ){
                    if( rhs == null ){
                        return 0;
                    } else {
                        return -1;
                    }
                } else if (rhs == null){
                    return 1;
                } else {
                    return lhs.getName().compareTo(rhs.getName());
                }
            }
        } );
        for( TrainStop stop : this.stops ){
            allLines.addAll(stop.getLines());
        }
        return allLines;
    }

    public double distance(double latitude, double longitude){
        return Math.sqrt(Math.pow(this.longitude - longitude, 2) + Math.pow(this.latitude - latitude, 2));
    }

    public boolean equals(TrainStation other){
        return this.name.equals(other.getName());
    }

    public TrainLine getRandomLine(){
        for( TrainStop stop : this.stops ){
            for( TrainLine line : stop.getLines() ){
                return line;
            }
        }
        return null;
    }

    public List<String> getLineNames(){
        SortedSet<String> lineNames = new TreeSet<String>();
        for( TrainLine line : this.getLines() ){
            lineNames.add(line.getName());
        }
        return Arrays.asList(lineNames.toArray(new String[]{}));
    }

    public boolean hasLine(TrainLine line){
        return this.getLines().contains(line);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Set<TrainStop> getStops() {
        return stops;
    }
}
