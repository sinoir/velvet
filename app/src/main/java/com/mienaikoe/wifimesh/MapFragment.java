package com.mienaikoe.wifimesh;


import com.mienaikoe.wifimesh.map.TrainView;
import com.mienaikoe.wifimesh.map.VectorInstruction;
import com.mienaikoe.wifimesh.map.VectorLineInstruction;
import com.mienaikoe.wifimesh.map.VectorPathInstruction;
import com.mienaikoe.wifimesh.map.VectorTextInstruction;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesse on 1/18/2015.
 */
public class MapFragment extends BaseFragment {

    private Context context;

    private ViewGroup rootView;
    private TrainStation station;
    private TrainView trainView;

    private TrainSystem system;
    private List<VectorInstruction> lines;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        this.context = inflater.getContext();

        this.trainView = new TrainView(this.context);
        this.trainView.setSystem( system );
        this.trainView.setLines( parsePaths(this.getResources().openRawResource(R.raw.vectors_lines)) );
        this.trainView.setLinesText( parsePaths(this.getResources().openRawResource(R.raw.vectors_lines_text)));
        this.trainView.setStationsText( parsePaths(this.getResources().openRawResource(R.raw.vectors_stations_text)));
        this.rootView.addView(this.trainView);

        return this.rootView;
    }


    private List<VectorInstruction> parsePaths( InputStream pathsXml ){
        List<VectorInstruction> instructions = new ArrayList<VectorInstruction>();

        try {
            // Parse Map Stops
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(pathsXml, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, "", "g");
            VectorTextInstruction activeTextInstruction = null;
            VectorTextInstruction subTextInstruction = null;
            String lastName = "";
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if( activeTextInstruction != null && parser.getEventType() == XmlPullParser.TEXT){
                    if( lastName.equals("text") ){
                        String text = parser.getText();
                        if( activeTextInstruction != null && text != null ) {
                            activeTextInstruction.setText(text.trim());
                        }
                    } else if (lastName.equals("tspan")){
                        String text = parser.getText();
                        if( text != null && subTextInstruction != null ) {
                            subTextInstruction.setText(parser.getText());
                        }
                    }
                    continue;
                }

                String name = parser.getName();

                if (parser.getEventType() == XmlPullParser.END_TAG ) {
                    if( name.equals("text") ){
                        if( activeTextInstruction != null && activeTextInstruction.hasText() ){
                            instructions.add(activeTextInstruction);
                        }
                        activeTextInstruction = null;
                    } else if (name.equals("tspan")){
                        if( subTextInstruction != null && subTextInstruction.hasText() ){
                            instructions.add(subTextInstruction);
                        }
                        subTextInstruction = null;
                    }
                } else if (parser.getEventType() == XmlPullParser.START_TAG) {
                    // Starts by looking for the entry tag
                    if (name.equals("line")) {
                        String color = parser.getAttributeValue("", "stroke");
                        if( color == null ){
                            color = "#000000";
                        }
                        String width = parser.getAttributeValue("","stroke-width");
                        if( width == null ){
                            width = "1";
                        }
                        instructions.add(new VectorLineInstruction(
                                Float.valueOf(parser.getAttributeValue("","x1")),
                                Float.valueOf(parser.getAttributeValue("","y1")),
                                Float.valueOf(parser.getAttributeValue("","x2")),
                                Float.valueOf(parser.getAttributeValue("","y2")),
                                Color.parseColor(color),
                                Float.valueOf(width)
                        ));
                    } else if (name.equals("path")) {
                        instructions.add(new VectorPathInstruction(
                                parser.getAttributeValue("","d"),
                                Color.parseColor(parser.getAttributeValue("", "stroke")),
                                Float.valueOf(parser.getAttributeValue("","stroke-width"))
                        ));
                    } else if (name.equals("text")) {
                        String transform = parser.getAttributeValue("","transform");
                        String color = parser.getAttributeValue("", "fill");
                        if( color == null ){
                            color = "#000000";
                        }
                        activeTextInstruction = new VectorTextInstruction(
                                "",
                                transform.substring(7, transform.length() - 1),
                                Color.parseColor(color),
                                FontEnum.HELVETICA_NEUE_MEDIUM.getTypeface(context)
                        );
                    } else if (name.equals("tspan")){
                        if( activeTextInstruction != null ) {
                            float offsetX = Float.valueOf(parser.getAttributeValue("","x"));
                            float offsetY = Float.valueOf(parser.getAttributeValue("","y"));
                            subTextInstruction = new VectorTextInstruction(activeTextInstruction);
                            subTextInstruction.addOffset(offsetX, offsetY);
                            String color = parser.getAttributeValue("", "fill");
                            if( color != null ){
                                subTextInstruction.setColor(Color.parseColor(color));
                            }
                        }
                    }
                }
                lastName = name;
            }
        } catch (XmlPullParserException ex ){
            Log.e(this.getClass().getSimpleName(), "Could not parse map");
            throw new IllegalArgumentException("Invalid Map Xml");
        } catch (IOException ex ){
            Log.e(this.getClass().getSimpleName(), "Culd not parse map");
            throw new IllegalArgumentException("IO Exception while parsing map");
        } finally {
            try {
                pathsXml.close();
            } catch (IOException ex ){
                Log.e(this.getClass().getSimpleName(), "Could not close streams");
            }
        }
        return instructions;
    }


    public void setSystem( TrainSystem system ){
        this.system = system;
    }

    public void setStation( TrainStation station ){
        if( station.hasRectangles() ) {
            this.trainView.setCenter(station.getViewX(), station.getViewY());
        }
        this.station = station;
    }




}
