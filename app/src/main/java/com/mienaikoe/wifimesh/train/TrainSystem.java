package com.mienaikoe.wifimesh.train;

import android.graphics.Matrix;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;
import com.google.transit.realtime.GtfsRealtime;
import com.mienaikoe.wifimesh.map.PathParser;
import com.mienaikoe.wifimesh.map.VectorInstruction;
import com.mienaikoe.wifimesh.map.VectorMapIngestor;
import com.mienaikoe.wifimesh.map.VectorRectangleInstruction;

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
import java.util.Collection;
import java.util.Date;
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

    private TreeSet<TrainLine> lines = new TreeSet<TrainLine>( new TrainLineComparator() );
    private Set<TrainStation> stations = new HashSet<TrainStation>();
    private Set<Trip> trips = new HashSet<Trip>();

    private Map<String, TrainStation> stopidStations = new HashMap<String, TrainStation>();


    public TrainSystem(
            InputStream stopsJsonStream,
            InputStream linesJsonStream,
            InputStream transfersJsonStream,
            InputStream entrancesJsonStream
            ) {
        JSONObject stopsJson = streamToJSON(stopsJsonStream);
        JSONObject transfersJson = streamToJSON(transfersJsonStream);
        JSONObject linesJson = streamToJSON(linesJsonStream);
        JSONObject entrancesJson = streamToJSON(entrancesJsonStream);

        Map<String, TrainStation> uniqueStations = new HashMap<String, TrainStation>();

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
                Set<TrainStation> transferStations = new HashSet<TrainStation>();
                for( int j=0; j<stationTransferSet.length(); j++ ){
                    String stopId = stationTransferSet.getString(j);
                    TrainStation aStation = stopidStations.get(stopId);
                    transferStations.add(aStation);
                }
                if( transferStations.size() > 1 ) {
                    TrainStation keepStation = null;
                    for (TrainStation aStation : transferStations ) {
                        if( keepStation == null ){
                            keepStation = aStation;
                        } else {
                            keepStation.transfer(aStation);
                            aStation.transfer(keepStation);
                            //this.stations.remove(aStation);
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
        } finally {
            try {
                stopsJsonStream.close();
                linesJsonStream.close();
                transfersJsonStream.close();
                entrancesJsonStream.close();
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


    public void clearTimnings() {
        this.trips.clear();
    }

    public void fillRectangles( VectorMapIngestor ingestor ){
        for( Map.Entry<String, VectorInstruction> namedInstruction : ingestor.getNamedInstructions().entrySet() ) {
            if (namedInstruction.getValue() instanceof VectorRectangleInstruction) {
                VectorRectangleInstruction instruction = (VectorRectangleInstruction) namedInstruction.getValue();
                TrainStation station = stopidStations.get(namedInstruction.getKey());
                if (station != null) {
                    station.addMapRectangle(instruction);
                }
            }
        }
    }

    public void fillTimings( GtfsRealtime.FeedMessage message ){
        for( int i=0; i<message.getEntityCount(); i++ ) {
            GtfsRealtime.TripUpdate update = message.getEntity(i).getTripUpdate();
            if( !update.isInitialized() ){
                continue;
            }

            String routeId = update.getTrip().getRouteId();

            TrainLine entityLine = null;
            for (TrainLine line : this.lines) {
                if (line.getName().equals(routeId)) {
                    entityLine = line;
                    break;
                }
            }

            String tripIdPortion = update.getTrip().getTripId().split("_")[1];
            if( tripIdPortion.length() > 1 ) {
                String directionStr = update.getTrip().getTripId().split("_")[1].substring(3, 4);
                TrainDirection direction = TrainDirection.fromString(directionStr);
                for (GtfsRealtime.TripUpdate.StopTimeUpdate stopTimeUpdate : message.getEntity(i).getTripUpdate().getStopTimeUpdateList()) {
                    String stopId = stopTimeUpdate.getStopId().substring(0, stopTimeUpdate.getStopId().length() - 1);
                    TrainStation station = this.stopidStations.get(stopId);
                    Trip trip = new Trip(entityLine, direction);
                    long timestamp = stopTimeUpdate.getArrival().getTime();
                    trip.addStationTiming(station, timestamp);
                    this.trips.add(trip);
                }
            } else {
                Log.w(this.getClass().getSimpleName(), "Invalid Trip Id from Sinoir: "+tripIdPortion);
            }
        }
    }

    public Date[] getTimings(TrainStation station, TrainLine line){
        Date northTiming = null;
        Date southTiming = null;
        for( Trip trip : this.trips ){
            if( trip.getLine() == line ){
                if( trip.getStationTimings().containsKey(station) ) {
                    Date date = trip.getStationTimings().get(station);
                    if (date.before(new Date())) {
                        continue;
                    }
                    if (trip.getDirection() == TrainDirection.NORTH) {
                        if (northTiming == null || date.before(northTiming)) {
                            northTiming = date;
                        }
                    } else {
                        if (southTiming == null || date.before(southTiming)) {
                            southTiming = date;
                        }
                    }
                }
            }
        }
        return new Date[]{northTiming, southTiming};
    }






    // Getters

    public Collection<TrainLine> getLines() {
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

    // TODO: Optimize
    public TrainStation closestEntrance(LatLng point){
        double closestRadius = 360F;
        TrainStation closestStation = null;
        for( TrainStation station : this.stations ){
            for( LatLng entrance : station.getEntrances() ){
                double radius = Math.sqrt(Math.pow(entrance.longitude - point.longitude, 2) + Math.pow(entrance.latitude - point.latitude, 2));
                if( radius < closestRadius ){
                    closestRadius = radius;
                    closestStation = station;
                }
            }
        }
        return closestStation;
    }



}
