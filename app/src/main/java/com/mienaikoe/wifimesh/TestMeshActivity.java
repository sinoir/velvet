package com.mienaikoe.wifimesh;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mienaikoe.wifimesh.mesh.BluetoothMeshField;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshIntent;

import java.util.ArrayList;

/**
 * Created by Jesse on 1/18/2015.
 */
public class TestMeshActivity extends Activity {

    private TextView stateView;
    private TextView advertisersView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.stateView = (TextView) findViewById(R.id.stateView);
        this.advertisersView = (TextView) findViewById(R.id.advertiserView);
    }



    private BroadcastReceiver bluetoothMeshServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> advertisers = intent.getStringArrayListExtra(BluetoothMeshField.ADVERTISERS.getLabel());
            String state = intent.getStringExtra(BluetoothMeshField.STATE.getLabel());
            Log.i(this.getClass().getName(), "Peers: " + advertisers.toString());
            Log.i(this.getClass().getName(), "State: " + state);

            stateView.setText(state);
            advertisersView.setText(advertisers.size() + " Advertisers");
        }
    };



    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(bluetoothMeshServiceReceiver, new IntentFilter(BluetoothMeshIntent.UPDATE.getLabel()));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(bluetoothMeshServiceReceiver);
    }


}
