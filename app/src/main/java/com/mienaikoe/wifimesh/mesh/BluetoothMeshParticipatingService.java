package com.mienaikoe.wifimesh.mesh;

import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;

import java.util.List;


// Fit for Android APIs 5+
public class BluetoothMeshParticipatingService extends BluetoothMeshService {

    private BluetoothLeAdvertiser advertiser;
    private BluetoothLeScanner scanner;

    private AdvertiseSettings advertiseSettings;
    private AdvertiseData advertiseData;






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
            advertisers.put(result.getDevice().getAddress(), result.getDevice());
            //result.getDevice().connectGatt(this, false, gattCallback);
            broadcastUpdate();
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            for( ScanResult result : results ){
                advertisers.put(result.getDevice().getAddress(), result.getDevice());
            }
            broadcastUpdate();
        }
    };



    public BluetoothMeshParticipatingService() {
        super();

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



    protected void setupBluetooth() {
        super.setupBluetooth();
        if( adapter != null ) {
            this.advertiser = adapter.getBluetoothLeAdvertiser();
            this.scanner = adapter.getBluetoothLeScanner();
        }
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
        if( this.state == BluetoothMeshState.SCANNING){
            scanner.stopScan(this.scanCallback);
            this.setState(BluetoothMeshState.ADVERTISING);
        }
        this.advertiser.startAdvertising(this.advertiseSettings, this.advertiseData, this.advertiseCallback);
    }


    protected void scan(){
        if( this.state == BluetoothMeshState.ADVERTISING ) {
            this.advertiser.stopAdvertising(this.advertiseCallback);
            this.setState(BluetoothMeshState.SCANNING);
        }
        this.scanner.startScan(scanCallback);
    }

}
