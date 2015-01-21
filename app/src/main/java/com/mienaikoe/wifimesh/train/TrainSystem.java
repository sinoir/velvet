package com.mienaikoe.wifimesh.train;

import android.util.Log;
import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainSystem {

    private Map<String, TrainStop> stops = new HashMap<String, TrainStop>();
    private Map<String, TrainLine> lines = new HashMap<String, TrainLine>();
    private Set<TrainStation> stations = new HashSet<TrainStation>();


    public TrainSystem( InputStream stopsJson, InputStream linesJson, InputStream transfersJson ) {
        parseStops(streamToJSON(stopsJson), streamToJSON(transfersJson));
        parseLines(streamToJSON(linesJson));
    }

    private JSONObject streamToJSON(InputStream inputStream) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return new JSONObject(total.toString());
        } catch (IOException ex ){
            Log.e(this.getClass().getSimpleName(), "Could Not Read JSON File");
            return null;
        } catch (JSONException ex ){
            Log.e(this.getClass().getSimpleName(), "Could Not Parse JSON File");
            return null;
        }
    }





    private void parseStops(JSONObject stopsJson, JSONObject transfersJson) {
        try {
            JSONArray transfers = transfersJson.getJSONArray("transfers");
            for( int i=0; i<transfers.length(); i++ ){
                JSONArray stationTransferSet = transfers.getJSONArray(i);
                TrainStation station = null;
                for( int j=0; j<stationTransferSet.length(); j++ ){
                    String stopId = stationTransferSet.getString(j);
                    JSONObject stopJson = stopsJson.getJSONObject(stopId);
                    if( station == null ){
                        station = new TrainStation( stopJson.getString("name") );
                        this.stations.add(station);
                    }
                    TrainStop stop = new TrainStop(
                            station,
                            stopJson.getDouble("latitude"),
                            stopJson.getDouble("longitude")
                    );
                    this.stops.put(stopId, stop);
                }
            }

            Iterator<String> stopsKeys = stopsJson.keys();
            while (stopsKeys.hasNext()) {
                String stopId = stopsKeys.next();
                JSONObject stopJson = stopsJson.getJSONObject(stopId);

                if( !this.stops.containsKey(stopId) ) {
                    TrainStation station = new TrainStation(stopJson.getString("name"));
                    this.stations.add(station);

                    TrainStop stop = new TrainStop(
                            station,
                            stopJson.getDouble("latitude"),
                            stopJson.getDouble("longitude")
                    );
                    this.stops.put(stopId, stop);
                }
            }
        } catch (JSONException ex ){
            Log.e(this.getClass().getSimpleName(), "Could not parse stops");
            throw new IllegalArgumentException("Invalid Stops Json");
        }
    }

    private void parseLines(JSONObject linesJson) {
        try {
            Iterator<String> linesKeys = linesJson.keys();
            while (linesKeys.hasNext()) {
                String lineId = linesKeys.next();
                JSONObject lineJson = linesJson.getJSONObject(lineId);

                TrainLine line = new TrainLine(
                        lineId,
                        parseLineDirection(lineJson.getJSONArray("N")),
                        parseLineDirection(lineJson.getJSONArray("S"))
                );

                this.lines.put(lineId, line);
            }
        } catch (JSONException ex ){
            Log.e(this.getClass().getSimpleName(), "Could not parse stops");
            throw new IllegalArgumentException("Invalid Stops Json");
        }
    }

    private ArrayList<TrainStop> parseLineDirection(JSONArray northJson) throws JSONException{
        ArrayList<TrainStop> parsedStations = new ArrayList<TrainStop>(northJson.length());
        for( int i=0; i< northJson.length(); i++ ){
            String stopId = northJson.getString(i);
            TrainStop stop = this.stops.get(stopId);
            if (stop == null){
                throw new IllegalArgumentException("Could not find train station with id "+stopId);
            }
            parsedStations.add(stop);
        }
        return parsedStations;
    }



    public TrainLine getLine(String name){
        return this.lines.get(name);
    }

    public TrainStation closestStation(double latitude, double longitude){
        double closestRadius = 180F;
        TrainStation closestStation = null;
        for( TrainStation station : this.stations ){
            double radius = station.distance(latitude, longitude);
            if( radius < closestRadius ){
                closestRadius = radius;
                closestStation = station;
            }
        }
        return closestStation;
    }



}
