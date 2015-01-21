package com.mienaikoe.wifimesh.train;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainLine {

    private final String name;

    private final ArrayList<TrainStop> northStops;
    private final ArrayList<TrainStop> southStops;




    public TrainLine(String name, ArrayList<TrainStop> northStops, ArrayList<TrainStop> southStops ){
        this.name = name;
        this.northStops = northStops;
        this.southStops = southStops;

        for( TrainStop stop : this.northStops ){
            stop.addLine(this);
        }
        for( TrainStop stop : this.southStops ){
            stop.addLine(this);
        }
    }


    public String getName() {
        return name;
    }



    public ArrayList<TrainStop> getNorthStops() {
        return northStops;
    }

    public ArrayList<TrainStop> getSouthStops() {
        return southStops;
    }

}
