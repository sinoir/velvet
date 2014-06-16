package com.delectable.mobile.api.models;

import org.json.JSONObject;

import java.util.ArrayList;

public abstract class ListingResponse extends BaseResponse {

    Boolean more;

    boolean invalidate;

    Boundaries boundaries;

    ArrayList<String> deletes;

    @Override
    public BaseResponse buildFromJson(JSONObject jsonObj) {
        JSONObject payload = jsonObj.optJSONObject("payload");
        ListingResponse newRegistration = buildFromJson(payload, this.getClass());
        newRegistration.invalidate = jsonObj.optBoolean("invalidate");
        return newRegistration;
    }

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public boolean getInvalidate() {
        return invalidate;
    }

    public void setInvalidate(boolean invalidate) {
        this.invalidate = invalidate;
    }

    public String getBoundariesFromBefore() {
        if (boundaries != null && boundaries.from != null) {
            return boundaries.from.before;
        }
        return null;
    }

    public String getBoundariesFromAfter() {
        if (boundaries != null && boundaries.from != null) {
            return boundaries.from.after;
        }
        return null;
    }

    public String getBoundariesFromSince() {
        if (boundaries != null && boundaries.from != null) {
            return boundaries.from.since;
        }
        return null;
    }

    public String getBoundariesToBefore() {
        if (boundaries != null && boundaries.to != null) {
            return boundaries.to.before;
        }
        return null;
    }

    public String getBoundariesToAfter() {
        if (boundaries != null && boundaries.to != null) {
            return boundaries.to.after;
        }
        return null;
    }

    public String getBoundariesToSince() {
        if (boundaries != null && boundaries.to != null) {
            return boundaries.to.since;
        }
        return null;
    }

    public Boundaries getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(Boundaries boundaries) {
        this.boundaries = boundaries;
    }

    public abstract void updateCombinedData();

    public abstract ArrayList<? extends BaseResponse> getSortedCombinedData();

    public abstract void combineWithPreviousListing(ListingResponse previousListing);

    public abstract ArrayList<? extends BaseResponse> getBefore();

    public abstract void setBefore(ArrayList<? extends BaseResponse> before);

    public abstract ArrayList<? extends BaseResponse> getAfter();

    public abstract void setAfter(ArrayList<? extends BaseResponse> after);

    public abstract ArrayList<? extends BaseResponse> getUpdates();

    public abstract void setUpdates(ArrayList<? extends BaseResponse> updates);

    public ArrayList<String> getDeletes() {
        return deletes;
    }

    public void setDeletes(ArrayList<String> deletes) {
        this.deletes = deletes;
    }

    @Override
    public String toString() {
        return "ListingResponse{" +
                "more=" + more +
                ", boundaries=" + boundaries +
                ", deletes=" + deletes +
                "} " + super.toString();
    }

    public static class Boundaries {

        Boundary from;

        Boundary to;

        @Override
        public String toString() {
            return "Boundaries{" +
                    "from=" + from +
                    ", to=" + to +
                    '}';
        }
    }

    public static class Boundary {

        String before;

        String after;

        String since;

        @Override
        public String toString() {
            return "Boundary{" +
                    "before='" + before + '\'' +
                    ", after='" + after + '\'' +
                    ", since=" + since +
                    '}';
        }
    }
}
