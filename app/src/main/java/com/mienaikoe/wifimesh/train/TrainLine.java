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

    private String name;
    private TreeMap<Integer,TrainStation> stations = new TreeMap<Integer,TrainStation>(
            new Comparator<Integer>(){
                @Override
                public int compare(Integer lhs, Integer rhs) {
                    return lhs.compareTo(rhs);
                }
            }
    );

    public TrainLine(String name){
        this.name = name;
    }


    public String getName() {
        return name;
    }



    public Collection<TrainStation> getStations() {
        return stations.values();
    }

    public void addStation(TrainStation station, Integer id){
        // This could be out of order
        if( !this.stations.containsKey(id) ) {
            this.stations.put(id, station);
            station.addLine(this, id);
        }
    }
}
