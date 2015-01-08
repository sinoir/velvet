package com.mienaikoe.wifimesh.mesh;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class BluetoothMeshService extends Service {

    private BluetoothManager manager;
    private BluetoothLeAdvertiser advertiser;
    private BluetoothLeScanner scanner;

    private AdvertiseSettings advertiseSettings;
    private AdvertiseData advertiseData;

    private static boolean STATE_ADVERTISING = true;
    private static boolean STATE_SCANNING = false;

    private boolean state = STATE_SCANNING;



    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.i(this.getClass().getSimpleName(), "Started Advertising");
            super.onStartSuccess(settingsInEffect);
        }
        @Override
        public void onStartFailure(int errorCode) {
            Log.e(this.getClass().getSimpleName(), "Could not Advertise: "+String.valueOf(errorCode));
            super.onStartFailure(errorCode);
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //result.getDevice().connectGatt(this, false, gattCallback);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }
    };

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
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
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED); // No clue what this is for
        settingsBuilder.setConnectable(false); // don't connect unless you're going to like chat or something. For now, just broadcast the train data you know.
        settingsBuilder.setTimeout(0); // Never Ending
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH); // greatest range
        this.advertiseSettings = settingsBuilder.build();

        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.setIncludeDeviceName(true);
        dataBuilder.setIncludeTxPowerLevel(false);
        this.advertiseData = dataBuilder.build();
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        this.manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = this.manager.getAdapter();
        if( bluetoothAdapter == null ){
            Log.e(this.getClass().getSimpleName(), "Bluetooth is not supported on this device");
            return;
        }

        this.advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        this.scanner = bluetoothAdapter.getBluetoothLeScanner();

        this.scan();
    }

    @Override
    public void onDestroy() {
        if( this.scanner != null ) {
            this.scanner.stopScan(this.scanCallback);
        }
        if( this.advertiser != null ) {
            this.advertiser.stopAdvertising(this.advertiseCallback);
        }
    }


    private void advertise(){
        if( this.state == STATE_SCANNING){
            scanner.stopScan(this.scanCallback);
            this.state = STATE_ADVERTISING;
        }
        this.advertiser.startAdvertising(this.advertiseSettings, this.advertiseData, this.advertiseCallback);
    }

    private void scan(){
        if( this.state == STATE_ADVERTISING ) {
            this.advertiser.stopAdvertising(this.advertiseCallback);
            this.state = STATE_SCANNING;
        }
        this.scanner.startScan(scanCallback);
    }

}
