package com.mienaikoe.wifimesh.train;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;

/**
 * Created by Jesse on 1/19/2015.
 */
public class TrainSystem {

    private Map<String, TrainLine> system = new HashMap<String, TrainLine>();


    public TrainSystem( InputStream systemXml ) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(systemXml, null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, null, "lines");
            TrainLine currentLine = null;

            while ( parser.next() != XmlPullParser.END_DOCUMENT ) {
                String tagName = parser.getName();
                int tagType = parser.getEventType();

                if ( tagType == XmlPullParser.END_TAG) {
                    if( tagName.equals("line") && currentLine != null ){
                        this.system.put(currentLine.getName(), currentLine);
                    }
                } else if( tagType == XmlPullParser.START_TAG){
                    // Starts by looking for the entry tag
                    if (tagName.equals("line")) {

                        String lineName = parser.getAttributeValue("","name");
                        currentLine = new TrainLine(lineName);
                    } else if (tagName.equals("station")) {
                        String stationName = parser.getAttributeValue("", "name");
                        TrainStation station = new TrainStation(stationName);
                        currentLine.addStation( station );
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

    public TrainLine getLine(String name){
        return this.system.get(name);
    }


}
