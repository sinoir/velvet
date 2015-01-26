package com.mienaikoe.wifimesh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if( !station.hasLine(this.currentLine) ) {
            this.currentLine = station.getRandomLine();
        }
        this.renderLine();
        this.lineSpinner.setSelection(this.lineSpinnerAdapter.getPosition(this.currentLine.getName()));
    }

    private TableRow renderStation(TrainStation station){
        TableRow newRow = new TableRow( getApplicationContext());
        newRow.setGravity(Gravity.CENTER_VERTICAL);
        if( station.equals(this.currentStation) ){
            newRow.setBackgroundColor(getResources().getColor(R.color.dark_gray));
        }
        newRow.setOnClickListener(new StationClickListener((StartupActivity) getActivity(), station));
        newRow.setMinimumHeight( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, context.getResources().getDisplayMetrics()) );

        // Connecting Lines
        int columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, context.getResources().getDisplayMetrics());
        GridLayout stationLines = new GridLayout( getApplicationContext() );
        stationLines.setColumnCount(6);
        stationLines.setMinimumWidth(6 * columnWidth);

        stationLines.setRotationY(180);

        if( station.getLines().size() > 1 ) {
            List<TrainLine> lines = new ArrayList<TrainLine>(station.getLines());
            Collections.reverse(lines);

            for( TrainLine line : lines ) {
                if( line.equals(this.currentLine) ){
                    continue;
                }
                TrainLineIcon icon = new TrainLineIcon(getApplicationContext(), line,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics())
                );
                icon.setRotationY(180);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setGravity(Gravity.RIGHT);
                params.setGravity(Gravity.CENTER_VERTICAL);
                params.setMargins(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics())
                );
                icon.setLayoutParams(params);

                stationLines.addView( icon );
            }
        }
        newRow.addView(stationLines);

        // Train Track Icon
        ImageView dotLine = new ImageView( getApplicationContext() );
        dotLine.setImageResource(R.drawable.ic_train_station);
        newRow.addView(dotLine);

        // Station Name
        TypefaceTextView stationName = new TypefaceTextView( getApplicationContext() );
        stationName.setText(station.getName());
        this.styleTypefaceTextView(stationName);
        stationName.setTextColor(getResources().getColor(R.color.white));

        newRow.addView(stationName);

        return newRow;
    }


    public void renderLine(){
        this.grid.removeAllViews();
        for( TrainStation lineStation : this.currentLine.getSouthStops() ){
            this.grid.addView( renderStation(lineStation) );
        }
    }



    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String lineName = (String)parent.getItemAtPosition(pos);
        this.currentLine = this.trainSystem.getLine(lineName);
        this.renderLine();
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


    private class StationClickListener implements View.OnClickListener {

        private StartupActivity parent;
        private TrainStation station;

        StationClickListener( StartupActivity parent, TrainStation station ){
            this.parent = parent;
            this.station = station;
        }

        @Override
        public void onClick(View v) {
            this.parent.setStation(this.station);
        }
    }

}
