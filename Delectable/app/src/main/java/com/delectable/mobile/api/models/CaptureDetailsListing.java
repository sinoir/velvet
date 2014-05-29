package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class CaptureDetailsListing extends ListingResponse {

    ArrayList<CaptureDetails> before;

    ArrayList<CaptureDetails> after;

    ArrayList<CaptureDetails> updates;

    @Override
    public ArrayList<CaptureDetails> getBefore() {
        return before;
    }

    @Override
    public void setBefore(ArrayList<? extends BaseResponse> before) {
        this.before = (ArrayList<CaptureDetails>) before;
    }

    @Override
    public ArrayList<CaptureDetails> getAfter() {
        return after;
    }

    @Override
    public void setAfter(ArrayList<? extends BaseResponse> after) {
        this.after = (ArrayList<CaptureDetails>) after;
    }

    @Override
    public ArrayList<CaptureDetails> getUpdates() {
        return updates;
    }

    @Override
    public void setUpdates(ArrayList<? extends BaseResponse> updates) {
        this.updates = (ArrayList<CaptureDetails>) updates;
    }

    @Override
    public String toString() {
        return "CaptureDetailsListing{" +
                "before=" + before +
                ", after=" + after +
                ", updates=" + updates +
                "} " + super.toString();
    }
}
