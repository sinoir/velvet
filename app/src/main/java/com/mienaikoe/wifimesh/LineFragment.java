package com.mienaikoe.wifimesh;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Jesse on 1/18/2015.
 */
public class LineFragment extends Fragment {

    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_line, container, false);

        this.context = inflater.getContext();

        TableLayout grid = (TableLayout) rootView.findViewById(R.id.station_list);
        for( int i=0; i<10; i++){
            grid.addView( makeStation("Station "+String.valueOf(i)) );
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


    private TableRow makeStation(String name){
        TableRow newRow = new TableRow( getApplicationContext());

        ImageView dotLine = new ImageView( getApplicationContext() );
        dotLine.setImageResource(R.drawable.ic_action_overflow);
        newRow.addView(dotLine);

        TextView stationName = new TextView( getApplicationContext() );
        stationName.setText(name);
        newRow.addView(stationName);

        return newRow;
    }


}
