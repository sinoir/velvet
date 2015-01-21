package com.mienaikoe.wifimesh;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private ArrayAdapter<CharSequence> lineSpinnerAdapter;
    private TableLayout grid;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_line, container, false);

        this.context = inflater.getContext();

        this.grid = (TableLayout) rootView.findViewById(R.id.station_list);
        this.lineSpinner = (Spinner) rootView.findViewById(R.id.line_spinner);

        populateLineSpinner();

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

    private void populateLineSpinner(){
        // populate line spinner because we can actually back the data with the Train System
        this.lineSpinnerAdapter = ArrayAdapter.createFromResource(this.getApplicationContext(),
                R.array.lines_array, android.R.layout.simple_spinner_item);

        this.lineSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.lineSpinner.setAdapter(this.lineSpinnerAdapter);
        this.lineSpinner.setOnItemSelectedListener(this);
    }



    private Context getApplicationContext(){
        return this.context;
    }




    public void setStation(TrainStation station){
        this.currentStation = station;
        TrainLine line = station.getRandomLine();
        this.lineSpinner.setSelection(this.lineSpinnerAdapter.getPosition(line.getName()));
    }

    private TableRow renderStation(TrainStation station){
        TableRow newRow = new TableRow( getApplicationContext());

        ImageView dotLine = new ImageView( getApplicationContext() );
        dotLine.setImageResource(R.drawable.ic_train_station);
        newRow.addView(dotLine);

        TypefaceTextView stationName = new TypefaceTextView( getApplicationContext() );
        stationName.setCustomFont(getApplicationContext(), "fonts/HelveticaNeue-Medium.otf");
        stationName.setText(station.getName());
        stationName.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, context.getResources().getDisplayMetrics()));
        stationName.setGravity(Gravity.CENTER_VERTICAL);
        if( station.equals(this.currentStation) ){
            stationName.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            stationName.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            stationName.setTextColor(getResources().getColor(R.color.white));
        } else {
            stationName.setTextColor(getResources().getColor(R.color.light_gray));
        }
        newRow.addView(stationName);

        return newRow;
    }



    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        this.grid.removeAllViews();
        String lineName = (String)parent.getItemAtPosition(pos);
        TrainLine line = this.trainSystem.getLine(lineName);
        for( TrainStop lineStop : line.getSouthStops() ){
            this.grid.addView( renderStation(lineStop.getStation()) );
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // ??
    }

}
