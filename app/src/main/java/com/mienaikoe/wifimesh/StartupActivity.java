package com.mienaikoe.wifimesh;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mienaikoe.wifimesh.mesh.MeshService;

import java.util.ArrayList;


public class StartupActivity extends Activity {

    private TextView texties;



    private BroadcastReceiver meshServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> peers = intent.getStringArrayListExtra(MeshService.FIELD_PEERS);
            String status = intent.getStringExtra(MeshService.FIELD_STATUS);
            Log.i(this.getClass().getName(), "Peers: " + peers.toString());
            Log.i(this.getClass().getName(), "Status: " + status);
            if( peers.size() > 0 ) {
                texties.setText("Peer Found: " + peers.get(0));
            } else {
                texties.setText("Finding Peers");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, MeshService.class));
        loadMap();
    }

    public void onResume() {
        super.onResume();
        registerReceiver(meshServiceReceiver, new IntentFilter(MeshService.INTENT_PEERS));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(meshServiceReceiver);
    }



    private void loadMap(){
        setContentView(R.layout.activity_startup_activity);

        texties = (TextView) findViewById(R.id.fullscreen_content);
        texties.setText("Finding Peers");
    }

}
