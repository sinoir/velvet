package com.mienaikoe.wifimesh.train;

import com.mienaikoe.wifimesh.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainLine {

    private static Map<String, Integer> BACKGROUND_COLORS = new HashMap<String, Integer>();
    static{
        BACKGROUND_COLORS.put("A", R.color.train_ace_blue);
        BACKGROUND_COLORS.put("C",R.color.train_ace_blue);
        BACKGROUND_COLORS.put("E",R.color.train_ace_blue);

        BACKGROUND_COLORS.put("B",R.color.train_bdfm_orange);
        BACKGROUND_COLORS.put("D",R.color.train_bdfm_orange);
        BACKGROUND_COLORS.put("F",R.color.train_bdfm_orange);
        BACKGROUND_COLORS.put("M",R.color.train_bdfm_orange);

        BACKGROUND_COLORS.put("7",R.color.train_7_purple);

        BACKGROUND_COLORS.put("G",R.color.train_g_green);

        BACKGROUND_COLORS.put("1",R.color.train_123_red);
        BACKGROUND_COLORS.put("2",R.color.train_123_red);
        BACKGROUND_COLORS.put("3",R.color.train_123_red);

        BACKGROUND_COLORS.put("4",R.color.train_456_green);
        BACKGROUND_COLORS.put("5",R.color.train_456_green);
        BACKGROUND_COLORS.put("6",R.color.train_456_green);

        BACKGROUND_COLORS.put("J",R.color.train_jz_brown);
        BACKGROUND_COLORS.put("Z",R.color.train_jz_brown);

        BACKGROUND_COLORS.put("N",R.color.train_nqr_yellow);
        BACKGROUND_COLORS.put("Q",R.color.train_nqr_yellow);
        BACKGROUND_COLORS.put("R",R.color.train_nqr_yellow);

        BACKGROUND_COLORS.put("L",R.color.train_l_gray);

        BACKGROUND_COLORS.put("GS",R.color.train_s_gray);
        BACKGROUND_COLORS.put("FS",R.color.train_s_gray);

        BACKGROUND_COLORS.put("SI",R.color.train_si_blue);
        BACKGROUND_COLORS.put("H",R.color.train_si_blue);
    }

    private static Map<String, Integer> FOREGROUND_COLORS = new HashMap<String, Integer>();
    static{
        FOREGROUND_COLORS.put("A",R.color.white);
        FOREGROUND_COLORS.put("C",R.color.white);
        FOREGROUND_COLORS.put("E",R.color.white);

        FOREGROUND_COLORS.put("B",R.color.white);
        FOREGROUND_COLORS.put("D",R.color.white);
        FOREGROUND_COLORS.put("F",R.color.white);
        FOREGROUND_COLORS.put("M",R.color.white);

        FOREGROUND_COLORS.put("7",R.color.white);

        FOREGROUND_COLORS.put("G",R.color.white);

        FOREGROUND_COLORS.put("1",R.color.white);
        FOREGROUND_COLORS.put("2",R.color.white);
        FOREGROUND_COLORS.put("3",R.color.white);

        FOREGROUND_COLORS.put("4",R.color.white);
        FOREGROUND_COLORS.put("5",R.color.white);
        FOREGROUND_COLORS.put("6",R.color.white);

        FOREGROUND_COLORS.put("J",R.color.white);
        FOREGROUND_COLORS.put("Z",R.color.white);

        FOREGROUND_COLORS.put("N",R.color.black);
        FOREGROUND_COLORS.put("Q",R.color.black);
        FOREGROUND_COLORS.put("R",R.color.black);

        FOREGROUND_COLORS.put("L",R.color.white);
        FOREGROUND_COLORS.put("GS",R.color.white);
        FOREGROUND_COLORS.put("FS",R.color.white);

        FOREGROUND_COLORS.put("SI",R.color.white);
        FOREGROUND_COLORS.put("H",R.color.white);
    }







    private final String name;

    private final ArrayList<TrainStation> northStops;
    private final ArrayList<TrainStation> southStops;




    public TrainLine(String name, ArrayList<TrainStation> northStops, ArrayList<TrainStation> southStops ){
        this.name = name;
        this.northStops = northStops;
        this.southStops = southStops;

        for( TrainStation station : this.northStops ){
            station.addLine(this);
        }
        for( TrainStation station : this.southStops ){
            station.addLine(this);
        }
    }


    public void replaceStation( TrainStation existingStation, TrainStation newStation ){
        int southIndex = this.southStops.indexOf(existingStation);
        if( southIndex != -1 ) {
            this.southStops.set(southIndex, newStation);
        }
        int northIndex = this.northStops.indexOf(existingStation);
        if( northIndex != -1 ) {
            this.northStops.set(northIndex, newStation);
        }
    }


    public String getName() {
        return name;
    }



    public ArrayList<TrainStation> getNorthStops() {
        return northStops;
    }

    public ArrayList<TrainStation> getSouthStops() {
        return southStops;
    }

    public int getForegroundColor(){
        return FOREGROUND_COLORS.get(this.name);
    }

    public int getBackgroundColor(){
        return BACKGROUND_COLORS.get(this.name);
    }

}
