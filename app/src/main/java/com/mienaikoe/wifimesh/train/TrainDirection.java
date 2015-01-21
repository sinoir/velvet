package com.mienaikoe.wifimesh.train;

/**
 * Created by Jesse on 1/21/2015.
 */
public enum TrainDirection {

    NORTH,
    SOUTH
    ;

    public static TrainDirection fromString(String code){
        if( code == "N"){
            return NORTH;
        } else if (code == "S") {
            return SOUTH;
        } else {
            return null;
        }
    }

}
