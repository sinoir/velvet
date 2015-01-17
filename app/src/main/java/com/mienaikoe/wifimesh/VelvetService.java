package com.mienaikoe.wifimesh;

import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.widget.Toast;

import com.mienaikoe.wifimesh.mesh.BluetoothMeshException;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshListeningMember;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshMember;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshParticipatingMember;

public class VelvetService extends Service {

    private BluetoothMeshMember mesh;


    public VelvetService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            this.errorNotSupported();
        } else {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            try {
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    this.mesh = new BluetoothMeshParticipatingMember(bluetoothManager, this);
                } else {
                    this.mesh = new BluetoothMeshListeningMember(bluetoothManager, this);
                }
            } catch(BluetoothMeshException ex){
                this.errorNotSupported();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    protected void errorNotSupported(){
        Toast.makeText(this.getApplicationContext(), "Your phone will be unable to connect to mesh because it does not support Bluetooth Low-Energy", Toast.LENGTH_SHORT).show();
    }
}
