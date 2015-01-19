package com.mienaikoe.wifimesh.mesh;

/**
 * Created by Jesse on 1/18/2015.
 */
public enum BluetoothMeshIntent {

    UPDATE("velvet.BluetoothMeshIntent.UPDATE"),
    ASK("velvet.BluetoothMeshIntent.ASK")
    ;


    private String label;

    BluetoothMeshIntent(String label){
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }

}
