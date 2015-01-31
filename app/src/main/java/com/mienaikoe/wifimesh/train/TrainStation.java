package com.mienaikoe.wifimesh.train;

import android.graphics.Rect;
import android.graphics.RectF;
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

    private Set<RectF> mapRectangles = new HashSet<RectF>();


    public TrainStation(String name, LatLng center){
        this.center = center;
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

        this.lines.addAll(other.getLines());
        this.mapRectangles.addAll(other.getMapRectangles());
    }

    public void transfer(TrainStation other){
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




    // Map View Dimensions

    public void addMapRectangle(RectF rect){
        this.mapRectangles.add(rect);
    }

    public void addMapRectangle(float x, float y, float width, float height){
        RectF rect = new RectF(x, y, x+width, y+height);
        this.addMapRectangle(rect);
    }

    public Set<RectF> getMapRectangles() {
        return mapRectangles;
    }

    public float getViewX(){
        float sumX = 0;
        for( RectF rect : this.mapRectangles ){
            sumX += rect.left;
        }
        return sumX / this.mapRectangles.size();
    }

    public float getViewY(){
        float sumY = 0;
        for( RectF rect : this.mapRectangles ){
            sumY += rect.top;
        }
        return sumY / this.mapRectangles.size();
    }

    public boolean hasRectangles(){
        return this.mapRectangles.size() > 0;
    }
}
