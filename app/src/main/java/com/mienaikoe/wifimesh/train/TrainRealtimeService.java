package com.mienaikoe.wifimesh.train;

import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.transit.realtime.GtfsRealtime;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshException;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshListeningMember;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshParticipatingMember;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class TrainRealtimeService extends Service {


    public TrainRealtimeService() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }









    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
