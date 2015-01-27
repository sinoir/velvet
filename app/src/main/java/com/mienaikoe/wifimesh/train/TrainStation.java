package com.mienaikoe.wifimesh.train;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

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

    private Set<TrainLine> lines = new TreeSet<TrainLine>( new TrainLineComparator() );

    private Set<LatLng> entrances = new HashSet<LatLng>();

    private LatLng center;



    private static double offsetLatitude = 40.95; // top-side latitude limit
    private static double offsetLongitude = -74.28; // left-side longitude limit

    private static float multiplierX = 1900;
    private static float multiplierY = 2900;

    private float viewX;
    private float viewY;


    public TrainStation(String name, LatLng center){
        this.center = center;

        this.viewX = (float)(this.center.longitude - offsetLongitude) * multiplierX;
        this.viewY = (float)(offsetLatitude - this.center.latitude) * multiplierY;

        this.name = name;
    }


    public void addLine(TrainLine line){
        this.lines.add(line);
    }

    public void addEntrance(LatLng entrance){
        this.entrances.add(entrance);
    }

    public Set<LatLng> getEntrances() {
        return entrances;
    }

    public String getName() {
        return name;
    }






    public Set<TrainLine> getLines() {
        return lines;
    }

    public double distance(LatLng point){
        return Math.sqrt(Math.pow(this.center.longitude - point.longitude, 2) + Math.pow(this.center.latitude - point.latitude, 2));
    }

    public boolean equals(TrainStation other){
        if( other == null ){
            return false;
        }
        return this.name.equals(other.getName());
    }

    public TrainLine getRandomLine(){
        for( TrainLine line : this.lines ){
            return line;
        }
        return null;
    }

    public List<String> getLineNames(){
        List<String> lineNames = new ArrayList<String>(this.lines.size());
        for( TrainLine line : this.getLines() ){
            lineNames.add(line.getName());
        }
        return lineNames;
    }

    public void merge(TrainStation other){
        for( TrainLine line : other.getLines() ){
            line.replaceStation(other, this);
        }
        this.center = new LatLng (
                (this.center.latitude + other.getLatitude()) / 2,
                (this.center.longitude + other.getLongitude()) / 2
        );

        this.viewX = (float)(this.center.longitude - offsetLongitude) * multiplierX;
        this.viewY = (float)(offsetLatitude - this.center.latitude) * multiplierY;

        this.lines.addAll(other.getLines());
    }

    public boolean hasLine(TrainLine line){
        return this.getLines().contains(line);
    }

    public double getLatitude() {
        return this.center.latitude;
    }

    public double getLongitude() {
        return this.center.longitude;
    }

    public LatLng getCenter() {
        return center;
    }

    public float getViewX() {
        return viewX;
    }

    public float getViewY() {
        return viewY;
    }
}
