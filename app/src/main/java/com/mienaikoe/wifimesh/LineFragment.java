package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jesse on 1/18/2015.
 */
public class LineFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, TrainIconAdapter.OnItemClickListener  {

    private static final String TAG = LineFragment.class.getSimpleName();

    private TrainSystem trainSystem;
    private TrainStation currentStation;
    private TrainLine currentLine;

    private RecyclerView mRecyclerView;
    private Spinner lineSpinner;
    private ArrayAdapter<String> lineSpinnerAdapter;
    private TableLayout grid;

    private TrainIconAdapter mAdapter = new TrainIconAdapter(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_line, container, false);

        setSupportActionBar((Toolbar)rootView.findViewById(R.id.toolbar));
        setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
       // mRecyclerView.setHasFixedSize(true);

        this.grid = (TableLayout) rootView.findViewById(R.id.station_list);
        this.lineSpinner = (Spinner) rootView.findViewById(R.id.line_spinner);

        // populate line spinner because we can actually back the data with the Train System
        this.lineSpinnerAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                trainSystem.getLineNames());
        this.lineSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.lineSpinner.setAdapter(this.lineSpinnerAdapter);
        this.lineSpinner.setOnItemSelectedListener(this);

        return rootView;
    }

    public void setTrainSystem(TrainSystem trainSystem){
        this.trainSystem = trainSystem;
        mAdapter.setItems(trainSystem.getLines());
    }

    public void setStation(TrainStation station){
        this.currentStation = station;
        if( !station.hasLine(this.currentLine) ) {
            this.currentLine = station.getRandomLine();
        }
        this.renderLine();
        this.lineSpinner.setSelection(this.lineSpinnerAdapter.getPosition(this.currentLine.getName()));
    }

    private TableRow renderStation(final TrainStation station){
        TableRow newRow = new TableRow(getActivity());
        newRow.setGravity(Gravity.CENTER_VERTICAL);
        if( station.equals(this.currentStation) ){
            newRow.setBackgroundColor(getResources().getColor(R.color.dark_gray));
        }
        newRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventBus.postSticky(new StationSelectEvent(station));
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
        newRow.setMinimumHeight( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, getActivity().getResources().getDisplayMetrics()) );

        // Connecting Lines
        int columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getActivity().getResources().getDisplayMetrics());
        GridLayout stationLines = new GridLayout(getActivity());
        stationLines.setColumnCount(6);
        stationLines.setMinimumWidth(6 * columnWidth);

        stationLines.setRotationY(180);

        if( station.getLines().size() > 1 ) {
            List<TrainLine> lines = new ArrayList<TrainLine>(station.getLines());
            Collections.reverse(lines);

            int smallSize = getResources().getDimensionPixelSize(R.dimen.train_icon_small);

            for( TrainLine line : lines ) {
                if( line.equals(this.currentLine) ){
                    continue;
                }

                TrainLineIcon icon = new TrainLineIcon(getActivity());
                icon.setTrainLine(line,smallSize);
                icon.setRotationY(180);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setGravity(Gravity.RIGHT);
                params.setGravity(Gravity.CENTER_VERTICAL);
                params.setMargins(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getActivity().getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getActivity().getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getActivity().getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getActivity().getResources().getDisplayMetrics())
                );
                icon.setLayoutParams(params);

                stationLines.addView( icon );
            }
        }
        newRow.addView(stationLines);

        // Train Track Icon
        ImageView dotLine = new ImageView(getActivity());
        dotLine.setImageResource(R.drawable.ic_train_station);
        newRow.addView(dotLine);

        // Station Name
        TypefaceTextView stationName = new TypefaceTextView(getActivity());
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

    @Override
    public void onItemClick(View view, TrainLine trainline) {
        currentLine = trainline;
        renderLine();
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
        textView.setTypeface(FontEnum.HELVETICA_NEUE_MEDIUM);
        textView.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getActivity().getResources().getDisplayMetrics()));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        textView.setPadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getActivity().getResources().getDisplayMetrics()),0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getActivity().getResources().getDisplayMetrics()),0);
    }



}
