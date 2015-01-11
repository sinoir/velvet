package com.mienaikoe.wifimesh.mesh;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public abstract class BluetoothMeshService extends Service {

    protected BluetoothManager manager;
    protected BluetoothAdapter adapter;

    protected Map<String, BluetoothDevice> advertisers = new HashMap<String, BluetoothDevice>();

    protected BluetoothMeshState state = BluetoothMeshState.DISABLED;







    protected BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }
    };





    public BluetoothMeshService() {
        super();
        Log.i(this.getClass().getSimpleName(), "Initializing Service");
    }


    protected void broadcastUpdate(){
        ArrayList<String> peerNames = new ArrayList<String>(advertisers.size());
        for( BluetoothDevice advertiser : advertisers.values() ){
            peerNames.add(advertiser.getName());
        }

        Intent intent = new Intent(BluetoothMeshField.INTENT.getLabel());
        intent.putExtra(BluetoothMeshField.ADVERTISERS.getLabel(), peerNames);
        intent.putExtra(BluetoothMeshField.STATE.getLabel(), this.state.getLabel());
        sendBroadcast(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        this.setupBluetooth();
        if( this.adapter != null ) {
            this.scan();
        }
    }


    protected void setupBluetooth() {
        this.manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.adapter = this.manager.getAdapter();
        if( this.adapter == null ){
            Log.e(this.getClass().getSimpleName(), "Bluetooth is not supported on this device.");
            this.setState(BluetoothMeshState.DISABLED);
        } else if( !this.adapter.isEnabled() ){
            this.adapter.enable();
        }
    }


    protected void scan(){
        // Meant to be overriden
    }

    protected void setState(BluetoothMeshState state){
        if( this.state != state ){
            this.state = state;
            broadcastUpdate();
        }
    }

}
