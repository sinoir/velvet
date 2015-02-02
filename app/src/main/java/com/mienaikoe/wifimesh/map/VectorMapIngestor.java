package com.mienaikoe.wifimesh.map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.util.Xml;

import com.mienaikoe.wifimesh.FontEnum;
import com.mienaikoe.wifimesh.TypefaceTextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jesse on 2/1/2015.
 */
public class VectorMapIngestor {

    private LinkedHashMap<String, List<VectorInstruction>> instructionss = new LinkedHashMap<String, List<VectorInstruction>>();
    private HashMap<String, VectorInstruction> namedInstructions = new HashMap<String, VectorInstruction>();
    private Context context;


    public VectorMapIngestor(Context context, String mapName){
        this.context = context;
        try {
            InputStream mapStream = context.getResources().getAssets().open("maps/"+mapName);
            parsePaths(mapStream);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Could not open requested map name: " + mapName);
            throw new IllegalArgumentException("Could not open requested map name: " + mapName);
        }
    }


    private Pattern ILLUSTRATOR_HEX_PATTERN = Pattern.compile("_x(\\w\\w)");


    private void parsePaths( InputStream pathsXml ){
        try {
            // Parse Map Stops
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(pathsXml, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, "", "svg");

            VectorTextInstruction activeTextInstruction = null;
            VectorTextInstruction subTextInstruction = null;
            String lastName = "";
            List<VectorInstruction> groupInstructions = new ArrayList<VectorInstruction>(0); //trash to prevent warnings

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();

                switch( parser.getEventType() ){
                    case( XmlPullParser.TEXT ):{
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
                        break;
                    }
                    case( XmlPullParser.END_TAG ): {
                        if (name.equals("text")) {
                            if (activeTextInstruction != null && activeTextInstruction.hasText()) {
                                groupInstructions.add(activeTextInstruction);
                            }
                            activeTextInstruction = null;
                        } else if (name.equals("tspan")) {
                            if (subTextInstruction != null && subTextInstruction.hasText()) {
                                groupInstructions.add(subTextInstruction);
                            }
                            subTextInstruction = null;
                        }
                        break;
                    }
                    case(XmlPullParser.START_TAG ):{
                        // Starts by looking for the entry tag
                        if( name.equals("g") ){
                            groupInstructions  = new ArrayList<VectorInstruction>();
                            this.instructionss.put( parser.getAttributeValue("","id"), groupInstructions );
                        } else if( name.equals("rect") ){
                            // making dimensions
                            float[] xy = new float[]{
                                    Float.valueOf(parser.getAttributeValue("","x")),
                                    Float.valueOf(parser.getAttributeValue("","y"))
                            };
                            // no clue if possible but would like some diagonalness
                            String transformStr = parser.getAttributeValue("","transform");
                            if( transformStr != null ) {
                                Matrix matrix = PathParser.parseTransform( transformStr );
                                matrix.mapPoints(xy);
                            }
                            String fillColor = parser.getAttributeValue("", "fill");
                            if( fillColor == null ){
                                fillColor = "#FFFFFF";
                            }
                            VectorRectangleInstruction instruction = new VectorRectangleInstruction(
                                    xy[0], xy[1],
                                    Float.valueOf(parser.getAttributeValue("","width")),
                                    Float.valueOf(parser.getAttributeValue("","height")),
                                    Color.parseColor(fillColor)
                            );
                            groupInstructions.add( instruction );

                            // parsing id for station identification
                            String shittyId = parser.getAttributeValue("","id");
                            if( shittyId != null ) {
                                String[] nameId = shittyId.split("_x5F");
                                StringBuilder rectIdBuilder = new StringBuilder();
                                Matcher rectIdMatcher = ILLUSTRATOR_HEX_PATTERN.matcher(nameId[0]);
                                while (rectIdMatcher.find()) {
                                    String hexCode = rectIdMatcher.group(1);
                                    rectIdBuilder.append((char) Integer.parseInt(hexCode, 16));
                                }
                                String rectId = rectIdBuilder.toString();
                                this.namedInstructions.put(rectId, instruction);
                            }
                        } else if (name.equals("line")) {
                            String color = parser.getAttributeValue("", "stroke");
                            if( color == null ){
                                color = "#000000";
                            }
                            String width = parser.getAttributeValue("","stroke-width");
                            if( width == null ){
                                width = "1";
                            }
                            groupInstructions.add(new VectorLineInstruction(
                                    Float.valueOf(parser.getAttributeValue("", "x1")),
                                    Float.valueOf(parser.getAttributeValue("", "y1")),
                                    Float.valueOf(parser.getAttributeValue("", "x2")),
                                    Float.valueOf(parser.getAttributeValue("", "y2")),
                                    Color.parseColor(color),
                                    Float.valueOf(width)
                            ));
                        } else if (name.equals("path")) {
                            String strokeColor = parser.getAttributeValue("", "stroke");
                            if( strokeColor == null ){
                                strokeColor = "#FFFFFF";
                            }
                            String strokeWidth = parser.getAttributeValue("", "stroke-width");
                            if( strokeWidth == null ){
                                strokeWidth = "1";
                            }
                            groupInstructions.add(new VectorPathInstruction(
                                    parser.getAttributeValue("", "d"),
                                    Color.parseColor(strokeColor),
                                    Float.valueOf(strokeWidth)
                            ));
                        } else if (name.equals("text")) {
                            String transform = parser.getAttributeValue("","transform");
                            String color = parser.getAttributeValue("", "fill");
                            if( color == null ){
                                color = "#000000";
                            }
                            String fontSize = parser.getAttributeValue("","font-size");
                            if( fontSize == null ){
                                fontSize = "9";
                            }
                            activeTextInstruction = new VectorTextInstruction(
                                    "", transform,
                                    Color.parseColor(color),
                                    FontEnum.HELVETICA_NEUE_MEDIUM.getTypeface(this.context),
                                    Float.valueOf(fontSize)
                            );
                        } else if (name.equals("tspan")){
                            if( activeTextInstruction != null ) {
                                float offsetX = Float.valueOf(parser.getAttributeValue("","x"));
                                float offsetY = Float.valueOf(parser.getAttributeValue("","y"));
                                String fontSize = parser.getAttributeValue("","font-size");
                                if( fontSize == null ){
                                    fontSize = "9";
                                }

                                subTextInstruction = new VectorTextInstruction(activeTextInstruction);
                                subTextInstruction.addOffset(offsetX, offsetY);
                                subTextInstruction.setFontSize( Float.valueOf(fontSize) );
                                String color = parser.getAttributeValue("", "fill");
                                if( color != null ){
                                    subTextInstruction.setColor(Color.parseColor(color));
                                }
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
    }


    public VectorInstruction getNamedInstruction(String name){
        return this.namedInstructions.get(name);
    }

    public HashMap<String, VectorInstruction> getNamedInstructions() {
        return namedInstructions;
    }

    public List<VectorInstruction> getAllInstructions(){
        List<VectorInstruction> ret = new ArrayList<VectorInstruction>();
        for( List<VectorInstruction> instructionGroup : this.instructionss.values() ){
            ret.addAll(instructionGroup);
        }
        return ret;
    }

    public List<VectorInstruction> getInstructionGroup(String groupName){
        return this.instructionss.get(groupName);
    }



}
