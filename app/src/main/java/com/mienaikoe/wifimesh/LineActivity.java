package com.mienaikoe.wifimesh;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mienaikoe.wifimesh.mesh.BluetoothMeshField;

/**
 * Created by Jesse on 1/18/2015.
 */
public class LineActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, VelvetService.class));

        loadLine();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void loadLine(){
        setContentView(R.layout.activity_line_activity);

        TableLayout grid = (TableLayout) findViewById(R.id.station_list);

        for( int i=0; i<10; i++){
            grid.addView( makeStation("Station "+String.valueOf(i)) );
        }
}

    private TableRow makeStation(String name){
        TableRow newRow = new TableRow(getApplicationContext());

        ImageView dotLine = new ImageView( getApplicationContext() );
        dotLine.setImageResource(R.drawable.ic_action_overflow);
        newRow.addView(dotLine);

        TextView stationName = new TextView( getApplicationContext() );
        stationName.setText(name);
        newRow.addView(stationName);

        return newRow;
    }


}
