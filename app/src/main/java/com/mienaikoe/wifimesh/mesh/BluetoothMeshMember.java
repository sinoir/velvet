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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public abstract class BluetoothMeshMember {

    protected BluetoothManager manager;
    protected BluetoothAdapter adapter;

    protected Service parent;

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





    public BluetoothMeshMember(BluetoothManager manager, Service parent) throws BluetoothMeshException {
        super();
        this.manager = manager;
        this.adapter = manager.getAdapter();
        if( this.adapter == null ){
            this.setState(BluetoothMeshState.DISABLED);
            throw new BluetoothMeshException("Bluetooth is not supported on this device");
        } else if( !this.adapter.isEnabled() ){
            this.adapter.enable();
        }
    }


    protected void broadcastUpdate(){
        ArrayList<String> peerNames = new ArrayList<String>(advertisers.size());
        for( BluetoothDevice advertiser : advertisers.values() ){
            peerNames.add(advertiser.getName());
        }

        Intent intent = new Intent(BluetoothMeshIntent.UPDATE.getLabel());
        intent.putExtra(BluetoothMeshField.ADVERTISERS.getLabel(), peerNames);
        intent.putExtra(BluetoothMeshField.STATE.getLabel(), this.state.getLabel());
        parent.sendBroadcast(intent); // TODO: REFACTOR AHH
    }


    public void scan(){
        // Meant to be overriden
    }

    public void advertise(){
        // Meant to be overriden
    }

    protected void setState(BluetoothMeshState state){
        if( this.state != state ){
            this.state = state;
            broadcastUpdate();
        }
    }

}
