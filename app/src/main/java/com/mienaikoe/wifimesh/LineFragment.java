package com.mienaikoe.wifimesh;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainStop;
import com.mienaikoe.wifimesh.train.TrainSystem;

/**
 * Created by Jesse on 1/18/2015.
 */
public class LineFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

    private Context context;

    private TrainSystem trainSystem;
    private TrainStation currentStation;
    private TrainLine currentLine;

    private Spinner lineSpinner;
    private ArrayAdapter<String> lineSpinnerAdapter;
    private TableLayout grid;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_line, container, false);

        this.context = inflater.getContext();

        this.grid = (TableLayout) rootView.findViewById(R.id.station_list);
        this.lineSpinner = (Spinner) rootView.findViewById(R.id.line_spinner);

        // populate line spinner because we can actually back the data with the Train System
        this.lineSpinnerAdapter = new ArrayAdapter<String>(
                this.getApplicationContext(),
                android.R.layout.simple_spinner_item,
                trainSystem.getLineNames());
        this.lineSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.lineSpinner.setAdapter(this.lineSpinnerAdapter);
        this.lineSpinner.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void setTrainSystem(TrainSystem trainSystem){
        this.trainSystem = trainSystem;
    }



    private Context getApplicationContext(){
        return this.context;
    }




    public void setStation(TrainStation station){
        this.currentStation = station;
        this.currentLine = station.getRandomLine();
        this.lineSpinner.setSelection(this.lineSpinnerAdapter.getPosition(this.currentLine.getName()));
    }

    private TableRow renderStation(TrainStation station){
        TableRow newRow = new TableRow( getApplicationContext());

        // Connecting Lines
        TypefaceTextView stationLines = new TypefaceTextView( getApplicationContext() );
        if( station.getLines().size() > 1 ) {
            StringBuilder stationNameBuilder = new StringBuilder();
            boolean delimit = false;
            for (String lineName : station.getLineNames()) {
                if( lineName.equals(this.currentLine.getName()) ){
                    continue;
                }
                if(delimit){
                    stationNameBuilder.append(" ");
                } else {
                    delimit = true;
                }
                stationNameBuilder.append(lineName);
            }
            stationLines.setText(stationNameBuilder.toString());
        }
        this.styleTypefaceTextView(stationLines);
        stationLines.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72, context.getResources().getDisplayMetrics()));
        stationLines.setGravity(Gravity.RIGHT);
        newRow.addView(stationLines);

        // Train Icon
        ImageView dotLine = new ImageView( getApplicationContext() );
        dotLine.setImageResource(R.drawable.ic_train_station);
        newRow.addView(dotLine);

        // Station Name
        TypefaceTextView stationName = new TypefaceTextView( getApplicationContext() );
        stationName.setText(station.getName());
        this.styleTypefaceTextView(stationName);
        if( station.equals(this.currentStation) ){
            stationName.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            stationName.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            stationName.setTextColor(getResources().getColor(R.color.white));
        } else {
            stationName.setTextColor(getResources().getColor(R.color.light_gray));
        }


        /*
        stationName.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        */


        newRow.addView(stationName);

        return newRow;
    }



    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        this.grid.removeAllViews();
        String lineName = (String)parent.getItemAtPosition(pos);
        this.currentLine = this.trainSystem.getLine(lineName);
        for( TrainStop lineStop : this.currentLine.getSouthStops() ){
            this.grid.addView( renderStation(lineStop.getStation()) );
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // ??
    }


    private void styleTypefaceTextView(TypefaceTextView textView){
        textView.setCustomFont(getApplicationContext(), "fonts/HelveticaNeue-Medium.otf");
        textView.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, context.getResources().getDisplayMetrics()));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        textView.setPadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()),0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()),0);
    }

}
