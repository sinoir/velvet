package com.mienaikoe.wifimesh.mesh;

/**
 * Created by Jesse on 1/10/2015.
 */
public enum BluetoothMeshState {

    DISABLED("Disabled"),           // Disabled means your phone doesn't support bluetooth
    ADVERTISING("Advertising"),     // Advertisers have data to give (Peripheral Role, Android 5+)
    SCANNING("Scanning")            // Scanners have data to recieve (Central Role, Android 4.3+)
    ;


    private String label;


    BluetoothMeshState(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
