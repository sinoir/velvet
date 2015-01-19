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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

/**
 * Created by Jesse on 1/18/2015.
 */
public class LineFragment extends Fragment {

    private Context context;
    private TrainSystem trainSystem;
    private TrainStation currentStation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_line, container, false);

        this.context = inflater.getContext();

        this.trainSystem = new TrainSystem( context.getResources().openRawResource(R.raw.train_system) );

        TableLayout grid = (TableLayout) rootView.findViewById(R.id.station_list);
        TrainLine eLine = this.trainSystem.getLine("E");
        currentStation = new TrainStation("Court Square – 23rd Street");
        for( TrainStation station : eLine.getStations() ){
            grid.addView( renderStation(station) );
        }

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


    private Context getApplicationContext(){
        return this.context;
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
            stationName.setTextColor(getResources().getColor(R.color.white));
        } else {
            stationName.setTextColor(getResources().getColor(R.color.light_gray));
        }
        newRow.addView(stationName);

        return newRow;
    }


}
