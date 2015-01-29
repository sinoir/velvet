package com.mienaikoe.wifimesh.train;

import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainSystem {

    private ArrayList<TrainLine> lines = new ArrayList<TrainLine>();
    private Set<TrainStation> stations = new HashSet<TrainStation>();


    public TrainSystem(
            InputStream stopsJsonStream,
            InputStream linesJsonStream,
            InputStream transfersJsonStream,
            InputStream entrancesJsonStream,
            InputStream blocksXmlStream
            ) {
        JSONObject stopsJson = streamToJSON(stopsJsonStream);
        JSONObject transfersJson = streamToJSON(transfersJsonStream);
        JSONObject linesJson = streamToJSON(linesJsonStream);
        JSONObject entrancesJson = streamToJSON(entrancesJsonStream);

        Map<String, TrainStation> uniqueStations = new HashMap<String, TrainStation>();
        Map<String, TrainStation> stopidStations = new HashMap<String, TrainStation>();

        try {

            // Set Stops
            Iterator<String> stopsKeys = stopsJson.keys();
            while (stopsKeys.hasNext()) {
                String stopId = stopsKeys.next();
                JSONObject stopJson = stopsJson.getJSONObject(stopId);

                String name = stopJson.getString("name");
                double latitude = stopJson.getDouble("latitude");
                double longitude = stopJson.getDouble("longitude");
                String uniquifier = latitude+":"+longitude;

                TrainStation station;
                if( uniqueStations.containsKey(uniquifier) ){
                    station = uniqueStations.get(uniquifier);
                    stopidStations.put(stopId, station);
                } else {
                    station = new TrainStation(
                            name, new LatLng(latitude, longitude)
                    );
                    this.stations.add(station);
                    uniqueStations.put(uniquifier, station);
                }
                stopidStations.put(stopId, station);
            }

            // Parse Map Stops
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(blocksXmlStream, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, "", "g");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("rect")) {
                    String stopId = parser.getAttributeValue("","id");
                    TrainStation station = stopidStations.get(stopId);
                    station.addMapRectangle(
                            Math.round(Float.valueOf(parser.getAttributeValue("","x"))),
                            Math.round(Float.valueOf(parser.getAttributeValue("","y"))),
                            Math.round(Float.valueOf(parser.getAttributeValue("","width"))),
                            Math.round(Float.valueOf(parser.getAttributeValue("","height")))
                    );
                }
            }


            // Set Lines
            Iterator<String> linesKeys = linesJson.keys();
            while (linesKeys.hasNext()) {
                String lineId = linesKeys.next();
                JSONObject lineJson = linesJson.getJSONObject(lineId);
                TrainLine line = new TrainLine(
                        lineId,
                        parseLineDirection(lineJson.getJSONArray("N"), stopidStations),
                        parseLineDirection(lineJson.getJSONArray("S"), stopidStations)
                );

                this.lines.add(line);
            }


            // Set Transfers
            JSONArray transfers = transfersJson.getJSONArray("transfers");
            for( int i=0; i<transfers.length(); i++ ){
                JSONArray stationTransferSet = transfers.getJSONArray(i);
                TrainStation station = null;
                Set<TrainStation> sameStations = new HashSet<TrainStation>();
                for( int j=0; j<stationTransferSet.length(); j++ ){
                    String stopId = stationTransferSet.getString(j);
                    TrainStation aStation = stopidStations.get(stopId);
                    sameStations.add(aStation);
                }
                if( sameStations.size() > 1 ) {
                    TrainStation keepStation = null;
                    for (TrainStation aStation : sameStations ) {
                        if( keepStation == null ){
                            keepStation = aStation;
                        } else {
                            keepStation.merge(aStation);
                            this.stations.remove(aStation);
                        }
                    }
                }
            }

            // Set entrances
            JSONArray entrances = entrancesJson.getJSONArray("entrances");
            for( int i=0; i< entrances.length(); i++ ){
                JSONObject coordsJson = entrances.getJSONObject(i);
                LatLng coords = new LatLng( coordsJson.getDouble("latitude"), coordsJson.getDouble("longitude") );
                TrainStation closest = this.closestStation(coords);
                closest.addEntrance(coords);
            }


        } catch (JSONException ex ){
            Log.e(this.getClass().getSimpleName(), "Could not parse stops");
            throw new IllegalArgumentException("Invalid Stops Json");
        } catch (XmlPullParserException ex ){
            Log.e(this.getClass().getSimpleName(), "Could not parse map");
            throw new IllegalArgumentException("Invalid Map Xml");
        } catch (IOException ex ){
            Log.e(this.getClass().getSimpleName(), "Culd not parse map");
            throw new IllegalArgumentException("IO Exception while parsing map");
        } finally {
            try {
                stopsJsonStream.close();
                linesJsonStream.close();
                transfersJsonStream.close();
                entrancesJsonStream.close();
                blocksXmlStream.close();
            } catch (IOException ex ){
                Log.e(this.getClass().getSimpleName(), "Could not close streams");
            }
        }

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


    private ArrayList<TrainStation> parseLineDirection(JSONArray directionJson, Map<String,TrainStation> stopidStations) throws JSONException{
        ArrayList<TrainStation> parsedStations = new ArrayList<TrainStation>(directionJson.length());
        for( int i=0; i< directionJson.length(); i++ ){
            String stopId = directionJson.getString(i);
            TrainStation station = stopidStations.get(stopId);
            if (station == null){
                throw new IllegalArgumentException("Could not find train station with id " + stopId);
            }
            parsedStations.add(station);
        }
        return parsedStations;
    }


    public List<TrainLine> getLines() {
        return lines;
    }

    public List<String> getLineNames(){
        SortedSet<String> lineNames = new TreeSet<String>();
        for( TrainLine line : this.getLines() ){
            lineNames.add(line.getName());
        }
        return Arrays.asList(lineNames.toArray(new String[]{}));
    }

    public Set<TrainStation> getStations() {
        return stations;
    }

    public TrainLine getLine(String name){
        for( TrainLine line : this.getLines() ){
            if( name.equals(line.getName()) ){
                return line;
            }
        }
        return null;
    }

    public TrainStation closestStation(LatLng point){
        double closestRadius = 360F;
        TrainStation closestStation = null;
        for( TrainStation station : this.stations ){
            double radius = station.distance(point);
            if( radius < closestRadius ){
                closestRadius = radius;
                closestStation = station;
            }
        }
        return closestStation;
    }



}
