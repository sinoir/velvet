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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jesse on 1/18/2015.
 */
public class LineFragment extends BaseFragment implements TrainIconAdapter.OnItemClickListener  {

    private static final String TAG = LineFragment.class.getSimpleName();

    private TrainSystem trainSystem;
    private TrainStation currentStation;
    private TrainLine currentLine;

    private RecyclerView mRecyclerView;
    private TableLayout mGrid;

    private TrainIconAdapter mAdapter = new TrainIconAdapter(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //look for event to consume
        InitEvent event = mEventBus.getStickyEvent(InitEvent.class);
        if (event == null) {
            return;
        }
        currentStation = event.station;
        //event is consumed, we can remove it now
        mEventBus.removeStickyEvent(event);
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
        mRecyclerView.setHasFixedSize(true);

        mGrid = (TableLayout) rootView.findViewById(R.id.station_list);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentStation != null) {
            setStation(currentStation);
        }
    }

    public void setTrainSystem(TrainSystem trainSystem){
        this.trainSystem = trainSystem;
        mAdapter.setItems(new ArrayList<TrainLine>(trainSystem.getLines()));
    }

    public void setStation(TrainStation station){
        this.currentStation = station;
        if( !station.hasLine(this.currentLine) ) {
            this.currentLine = station.getRandomLine();
        }
        this.renderLine();
    }

    private TableRow renderStation(final TrainStation station){
        LayoutInflater inflater = getActivity().getLayoutInflater();

        TableRow newRow = new TableRow(getActivity());
        newRow.setGravity(Gravity.CENTER_VERTICAL);
        newRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventBus.postSticky(new StationSelectEvent(station));
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
        newRow.setBackgroundResource(R.drawable.bg_transparent_to_light_white_opacity);
        if( station.equals(this.currentStation) ) {
            newRow.setSelected(true);
        }

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

            for( TrainLine line : lines ) {
                if( line.equals(this.currentLine) ){
                    continue;
                }

                TrainLineIcon icon = (TrainLineIcon)inflater.inflate(R.layout.lineview_train_icon, stationLines, false);
                icon.setTrainLine(line);
                icon.setRotationY(180);
                stationLines.addView(icon);
            }
        }
        newRow.addView(stationLines);

        // Train Track Icon
        ImageView dotLine = new ImageView(getActivity());
        dotLine.setImageResource(R.drawable.ic_train_station);
        newRow.addView(dotLine);

        TypefaceTextView stationName = (TypefaceTextView)inflater.inflate(R.layout.station_name, newRow, false);
        stationName.setText(station.getName());
        newRow.addView(stationName);

        return newRow;
    }


    public void renderLine(){
        mGrid.removeAllViews();
        for( TrainStation lineStation : this.currentLine.getSouthStops() ){
            mGrid.addView( renderStation(lineStation) );
        }
    }

    @Override
    public void onItemClick(View view, TrainLine trainline, int position) {
        mAdapter.setSelectedPosition(position);
        mAdapter.notifyDataSetChanged();
        currentLine = trainline;
        renderLine();
    }

    public static class InitEvent {
        private TrainStation station;

        public InitEvent(TrainStation station) {
            this.station = station;
        }
    }

}
