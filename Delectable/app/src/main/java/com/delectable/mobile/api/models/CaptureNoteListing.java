package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class CaptureNoteListing extends ListingResponse {

    ArrayList<CaptureNote> before;

    ArrayList<CaptureNote> after;

    ArrayList<CaptureNote> updates;

    @Override
    public ArrayList<CaptureNote> getBefore() {
        return before;
    }

    @Override
    public void setBefore(ArrayList<? extends BaseResponse> before) {
        this.before = (ArrayList<CaptureNote>) before;
    }

    @Override
    public ArrayList<CaptureNote> getAfter() {
        return after;
    }

    @Override
    public void setAfter(ArrayList<? extends BaseResponse> after) {
        this.after = (ArrayList<CaptureNote>) after;
    }

    @Override
    public ArrayList<CaptureNote> getUpdates() {
        return updates;
    }

    @Override
    public void setUpdates(ArrayList<? extends BaseResponse> updates) {
        this.updates = (ArrayList<CaptureNote>) updates;
    }

    @Override
    public String toString() {
        return "CaptureNoteListing{" +
                "before=" + before +
                ", after=" + after +
                ", updates=" + updates +
                "} " + super.toString();
    }
}
