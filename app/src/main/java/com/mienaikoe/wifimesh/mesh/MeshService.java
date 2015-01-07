package com.mienaikoe.wifimesh.mesh;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/*
 * Mesh Service
 */

public class MeshService extends Service {

    private WifiP2pManager meshManager;
    private WifiP2pManager.Channel meshChannel;

    private WiFiDirectBroadcastReceiver wifiDirectBroadcastReceiver;
    private IntentFilter wifiDirectIntents;

    private Map<String,WifiP2pDevice> peerList;
    private static short MAX_PEERS = 6;

    public static String FIELD_PEERS = "com.mienaikoe.wifimesh.fields.peers";
    public static String FIELD_STATUS = "com.mienaikoe.wifimesh.fields.status";
    public static String INTENT_PEERS = "com.mienaikie.wifimesh.intents.peers";



    public MeshService( ) {

    }

    private void initialize(){
        // Instantiate Vital Transport Parts
        meshManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        meshChannel = meshManager.initialize(this, getMainLooper(), null);

        // Create a Receiver
        wifiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver();

        wifiDirectIntents = new IntentFilter();
        wifiDirectIntents.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiDirectIntents.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiDirectIntents.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiDirectIntents.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(wifiDirectBroadcastReceiver, wifiDirectIntents);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /* register the broadcast receiver with the intent values to be matched */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( meshManager == null ){
            initialize();
        }

        // Look For Peers
        meshManager.discoverPeers(meshChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Handled by WiFiDirectBroadcastReceiver
            }
            @Override
            public void onFailure(int reasonCode) {
                Log.e(this.getClass().getName(), "Could Not Discover Peers: " + String.valueOf(reasonCode));
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    /* unregister the broadcast receiver */
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiDirectBroadcastReceiver);
    }





    private void connectToPeer(final WifiP2pDevice device){
        //obtain a peer from the WifiP2pDeviceList
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        meshManager.connect(meshChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                peerList.put(device.deviceAddress, device);
            }
            @Override
            public void onFailure(int reason) {
                Log.e(this.getClass().getName(), "Could Not Connect to Peer: " + String.valueOf(reason));
            }
        });
    }





    /**
     * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
     */
    class WiFiDirectBroadcastReceiver extends BroadcastReceiver implements WifiP2pManager.PeerListListener {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    // Wifi P2P is enabled
                } else {
                    // Wi-Fi P2P is not enabled
                }
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                // request available peers from the wifi p2p manager. This is an
                // asynchronous call and the calling activity is notified with a
                // callback on PeerListListener.onPeersAvailable()
                if (meshManager != null) {
                    meshManager.requestPeers(meshChannel, this);
                }
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
            }
        }

        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
           for( WifiP2pDevice device : peers.getDeviceList() ){
               if( peerList.size() >= MAX_PEERS ){
                   break;
               }
               if( peerList.containsKey(device.deviceAddress) ){
                   continue;
               }
               connectToPeer(device);
            }

            ArrayList<String> peerNames = new ArrayList<String>(peerList.size());
            Intent intent = new Intent(MeshService.INTENT_PEERS);
            for( WifiP2pDevice peer : peerList.values() ){
                peerNames.add(peer.deviceName);
            }
            intent.putExtra(MeshService.FIELD_PEERS, peerNames);
            intent.putExtra(MeshService.FIELD_STATUS, "New Peers");
            sendBroadcast(intent);
        }
    }
}
