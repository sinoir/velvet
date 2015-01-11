package com.mienaikoe.wifimesh;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mienaikoe.wifimesh.mesh.BluetoothMeshField;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshListeningService;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshParticipatingService;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshService;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshState;

import java.util.ArrayList;


public class StartupActivity extends Activity {

    private TextView stateView;
    private TextView advertisersView;
    private ImageView mapView;


/*
    private BroadcastReceiver wifiMeshServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> peers = intent.getStringArrayListExtra(WifiMeshService.FIELD_PEERS);
            String status = intent.getStringExtra(WifiMeshService.FIELD_STATUS);
            Log.i(this.getClass().getName(), "Peers: " + peers.toString());
            Log.i(this.getClass().getName(), "Status: " + status);
            if( peers.size() > 0 ) {
                texties.setText("Peer Found: " + peers.get(0));
            } else {
                texties.setText("Finding Peers");
            }
        }
    };
*/

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Your phone will be unable to connect to mesh because it does not support Bluetooth Low-Energy", Toast.LENGTH_SHORT).show();
        } else*/ if (android.os.Build.VERSION.SDK_INT >= 21) {
            startService(new Intent(this, BluetoothMeshParticipatingService.class));
        } else {
            startService(new Intent(this, BluetoothMeshListeningService.class));
        }

        loadMap();
    }

    public void onResume() {
        super.onResume();
        registerReceiver(bluetoothMeshServiceReceiver, new IntentFilter(BluetoothMeshField.INTENT.getLabel()));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(bluetoothMeshServiceReceiver);
    }



    private void loadMap(){
        setContentView(R.layout.activity_startup_activity);

        this.stateView = (TextView) findViewById(R.id.stateView);
        this.advertisersView = (TextView) findViewById(R.id.advertiserView);
        this.mapView = (ImageView) findViewById(R.id.mapView);

        // Map Stuff?
        Log.i(this.getClass().getSimpleName(), "Map Loaded");
    }

}
