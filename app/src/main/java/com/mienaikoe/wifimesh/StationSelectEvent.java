package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainStation;

public class StationSelectEvent {

    private TrainStation mStation;

    public StationSelectEvent(TrainStation station) {
        mStation = station;
    }

    public TrainStation getStation() {
        return mStation;
    }
}
