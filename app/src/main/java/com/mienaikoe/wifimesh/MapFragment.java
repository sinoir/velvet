package com.mienaikoe.wifimesh;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mienaikoe.wifimesh.map.TrainView;
import com.mienaikoe.wifimesh.map.VectorInstruction;
import com.mienaikoe.wifimesh.map.VectorLineInstruction;
import com.mienaikoe.wifimesh.map.VectorPathInstruction;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesse on 1/18/2015.
 */
public class MapFragment extends Fragment {

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
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("line")) {
                    Paint painter = new Paint();
                    painter.setColor(Color.parseColor(parser.getAttributeValue("", "stroke")));
                    painter.setStrokeWidth(Float.valueOf(parser.getAttributeValue("","stroke-width")));

                    instructions.add(new VectorLineInstruction(
                            Float.valueOf(parser.getAttributeValue("","x1")),
                            Float.valueOf(parser.getAttributeValue("","y1")),
                            Float.valueOf(parser.getAttributeValue("","x2")),
                            Float.valueOf(parser.getAttributeValue("","y2")),
                            painter
                    ));
                } else if (name.equals("path")) {
                    Paint painter = new Paint();
                    painter.setColor(Color.parseColor(parser.getAttributeValue("","stroke")));
                    painter.setStrokeWidth(Float.valueOf(parser.getAttributeValue("","stroke-width")));

                    instructions.add(new VectorPathInstruction(
                            parser.getAttributeValue("","d"),
                            painter
                    ));
                }
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
        this.trainView.setCenter( station.getViewX(), station.getViewY() );
        this.station = station;
    }




}
