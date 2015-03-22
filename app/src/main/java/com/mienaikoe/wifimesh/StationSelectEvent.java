package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainStation;

public class StationSelectEvent {

    private TrainStation mStation;
    private boolean mMoveTo;

    public StationSelectEvent(TrainStation station, boolean moveTo) {
        mMoveTo = moveTo;
        mStation = station;
    }

    public TrainStation getStation() {
        return mStation;
    }

    public boolean isMoveTo() {
        return mMoveTo;
    }
}
