package com.mienaikoe.wifimesh.mesh;

import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.UUID;


// Fit for Android APIs 5+
public class BluetoothMeshParticipatingMember extends BluetoothMeshMember {

    private BluetoothLeAdvertiser advertiser;
    private BluetoothLeScanner scanner;

    private AdvertiseSettings advertiseSettings;
    private AdvertiseData advertiseData;

    private static UUID VELVET_SERVICE_UUID = new UUID(0x92FA46, 0x92FA46); // Make something up? I have no clue
    private static ParcelUuid VELVET_SERVICE_UUID_PARCEL = new ParcelUuid(VELVET_SERVICE_UUID);



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

            // We're kind of getting into a different realm here. Consider using a different class
            byte[] scanTimestamp = result.getScanRecord().getServiceData(VELVET_SERVICE_UUID_PARCEL);
            // scanTimestamp will be a timestamp for the most up-to-date information the phone has.
            if( scanTimestamp.length < 4 ){
                Log.e(this.getClass().getSimpleName(), "Scan shows an invalid timestamp for device: "+result.getDevice().getName());
                return;
            }
            ByteBuffer timestampBuffer = ByteBuffer.allocate(Long.SIZE).put(scanTimestamp);
            timestampBuffer.flip();
            long timestamp = timestampBuffer.getLong();
            // TODO: if scanTimestamp is more up-to-date than the current information, ask phone for info from GATT and then become an advertiser yourself. This is the majority case.
            // TODO: if scanTimestamp is less up-to-date than the current information, become an advertiser for your info. This should be rare.
            // TODO: if scanTimestamp is equal date than the current information, do nothing because that means you're both good to go.

            /*
            // For connecting with GATT in order to have a continuous conversation
            BluetoothGatt gatt = result.getDevice().connectGatt(context, false, gattCallback);
            BluetoothGattService gattService = gatt.getService(VELVET_SERVICE_UUID);
            BluetoothGattCharacteristic gattCharac = gattService.getCharacteristic(VELVET_SERVICE_UUID);
            */

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



    public BluetoothMeshParticipatingMember(BluetoothManager manager, Service parent) throws BluetoothMeshException {
        super(manager, parent);

        this.advertiser = adapter.getBluetoothLeAdvertiser();
        this.scanner = adapter.getBluetoothLeScanner();

        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED); // No clue what this is for
        settingsBuilder.setConnectable(false); // don't connect unless you're going to like chat or something. For now, just broadcast the train data you know.
        settingsBuilder.setTimeout(0); // Never Ending
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH); // greatest range
        this.advertiseSettings = settingsBuilder.build();

        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.setIncludeDeviceName(true);
        dataBuilder.setIncludeTxPowerLevel(false);
        dataBuilder.addServiceUuid(VELVET_SERVICE_UUID_PARCEL);
        dataBuilder.addServiceData(VELVET_SERVICE_UUID_PARCEL, ByteBuffer.allocate(Long.SIZE).putLong(new Date().getTime()).array() );
        this.advertiseData = dataBuilder.build();
    }


    public void stop() {
        if( this.scanner != null ) {
            this.scanner.stopScan(this.scanCallback);
        }
        if( this.advertiser != null ) {
            this.advertiser.stopAdvertising(this.advertiseCallback);
        }
    }


    public void advertise(){
        if( this.state == BluetoothMeshState.SCANNING){
            scanner.stopScan(this.scanCallback);
            this.setState(BluetoothMeshState.ADVERTISING);
        }
        this.advertiser.startAdvertising(this.advertiseSettings, this.advertiseData, this.advertiseCallback);
    }


    public void scan(){
        if( this.state == BluetoothMeshState.ADVERTISING ) {
            this.advertiser.stopAdvertising(this.advertiseCallback);
            this.setState(BluetoothMeshState.SCANNING);
        }
        this.scanner.startScan(scanCallback);
    }

}
