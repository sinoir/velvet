package com.mienaikoe.wifimesh.mesh;

/**
 * Created by Jesse on 1/17/2015.
 */
public class BluetoothMeshException extends Exception {

    public BluetoothMeshException(String detailMessage) {
        super(detailMessage);
    }

    public BluetoothMeshException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
