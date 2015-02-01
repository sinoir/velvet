package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainSystem;

/**
 * Rudimentary static TrainSystem cache.
 */
public class TrainSystemModel {

    private static TrainSystem mSystem;

    public static void setTrainSystem(TrainSystem system) {
        mSystem = system;
    }

    public static TrainSystem getTrainSystem() {
        return mSystem;
    }
}
