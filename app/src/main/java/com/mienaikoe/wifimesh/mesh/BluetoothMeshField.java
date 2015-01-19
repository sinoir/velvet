package com.mienaikoe.wifimesh.mesh;

/**
 * Created by Jesse on 1/10/2015.
 */
public enum BluetoothMeshField {

    ADVERTISERS("velvet.BluetoothMeshField.ADVERTISERS"),
    STATE("velvet.BluetoothMeshField.STATE"),
    ;

    private String label;

    BluetoothMeshField(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
