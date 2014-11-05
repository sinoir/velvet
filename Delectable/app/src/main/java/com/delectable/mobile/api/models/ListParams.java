package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

public class ListParams {

    private String type;

    @SerializedName("z-score")
    private double zScore;

    public String getType() {
        return type;
    }

    public double getZScore() {
        return zScore;
    }
}
