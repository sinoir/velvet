package com.mienaikoe.wifimesh.train;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainSystem {

    private Map<String, TrainLine> lines = new HashMap<String, TrainLine>();

    private Map<String, TrainStation> stations = new HashMap<String, TrainStation>();


    public TrainSystem( InputStream stationsXml ) {
        parseStations(stationsXml);
    }


    private void parseStations(InputStream systemXml) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(systemXml, null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, null, "stations");
            TrainStation currentStation = null;

            while ( parser.next() != XmlPullParser.END_DOCUMENT ) {
                String tagName = parser.getName();
                int tagType = parser.getEventType();

                if ( tagType == XmlPullParser.END_TAG) {
                    if( tagName.equals("station") && currentStation != null ){
                        this.stations.put(currentStation.getName(), currentStation);
                    }
                } else if( tagType == XmlPullParser.START_TAG){
                    // Starts by looking for the entry tag
                    if (tagName.equals("station")) {
                        String stationName = parser.getAttributeValue("","name");
                        float lineLatitude = Float.valueOf(parser.getAttributeValue("","latitude"));
                        float lineLongitude = Float.valueOf(parser.getAttributeValue("","longitude"));
                        currentStation = new TrainStation(stationName, lineLatitude, lineLongitude);
                    } else if (tagName.equals("line")) {
                        String lineName = parser.getAttributeValue("","name");
                        Integer lineStationId = Integer.valueOf(parser.getAttributeValue("", "id"));
                        TrainLine line;
                        if( lines.containsKey(lineName) ){
                            line = lines.get(lineName);
                        } else {
                            line = new TrainLine(lineName);
                            lines.put(lineName, line);
                        }
                        currentStation.addLine(line, lineStationId);
                    }
                }
            }
        } catch( XmlPullParserException ex ) {
            Log.e(this.getClass().getSimpleName(), "Could not Parse XML File. Please check train_system.xml");
            throw new IllegalArgumentException("Could not Parse Assets", ex);
        } catch( IOException ex ){
            Log.e(this.getClass().getSimpleName(), "Could not Locate XML File. Please check train_system.xml");
            throw new IllegalArgumentException("Could not Locate Assets", ex);
        } finally {
            if( systemXml != null) {
                try {
                    systemXml.close();
                } catch( IOException ex ){
                    Log.e(this.getClass().getSimpleName(), "Could not close Xml File. Please check train_system.xml");
                    throw new IllegalArgumentException("Could not Locate Assets");
                }
            }
        }
    }



    public TrainStation getStation(String name) {
        return this.stations.get(name);
    }

    public TrainLine getLine(String name){
        return this.lines.get(name);
    }

    public TrainStation closestStation(float latitude, float longitude){
        float closestRadius = 180F;
        TrainStation closestStation = null;
        for( TrainStation station : this.stations.values() ){
            float radius = station.distance(latitude, longitude);
            if( radius < closestRadius ){
                closestRadius = radius;
                closestStation = station;
            }
        }
        return closestStation;
    }



}
