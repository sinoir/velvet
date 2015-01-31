package com.mienaikoe.wifimesh.train;

/**
 * Created by Jesse on 1/21/2015.
 */
public enum TrainDirection {

    NORTH,
    SOUTH
    ;

    public static TrainDirection fromString(String code){
        if( code.equals("N")){
            return NORTH;
        } else if (code.equals("S")) {
            return SOUTH;
        } else {
            return null;
        }
    }

    public static TrainDirection fromStopId(String stopId){
        String lastChar = stopId.substring(stopId.length()-2, stopId.length()-1);
        return fromString(lastChar);
    }

}
