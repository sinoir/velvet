package com.mienaikoe.wifimesh.mesh;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;


// Fit for Android APIs 4.3+
public class BluetoothMeshListeningService extends BluetoothMeshService {

    private Handler handler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;


    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    advertisers.put(device.getAddress(), device);
                    broadcastUpdate();
                }
            };



    public BluetoothMeshListeningService() {
        super();
    }




    @Override
    public void onDestroy() {
        adapter.stopLeScan(mLeScanCallback);
    }

    protected void scan(){
        // Stops scanning after a pre-defined scan period.
        this.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.stopLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);
        this.setState(BluetoothMeshState.SCANNING);
        this.adapter.startLeScan(this.mLeScanCallback);
    }

}
