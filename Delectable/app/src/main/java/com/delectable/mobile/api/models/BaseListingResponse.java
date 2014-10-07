package com.delectable.mobile.api.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseListingResponse<T> {

    private static final String TAG = BaseListingResponse.class.getSimpleName();

    protected Boundaries boundaries;

    protected ArrayList<T> before;

    protected ArrayList<T> after;

    protected ArrayList<T> updates;

    protected ArrayList<String> deletes;

    protected boolean more;

    protected String e_tag;

    protected String context;


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

    public ArrayList<T> getBefore() {
        return before;
    }

    public void setBefore(ArrayList<T> before) {
        this.before = before;
    }

    public ArrayList<T> getAfter() {
        return after;
    }

    public void setAfter(ArrayList<T> after) {
        this.after = after;
    }

    public ArrayList<T> getUpdates() {
        return updates;
    }

    public void setUpdates(ArrayList<T> updates) {
        this.updates = updates;
    }

    public ArrayList<String> getDeletes() {
        return deletes;
    }

    public void setDeletes(ArrayList<String> deletes) {
        this.deletes = deletes;
    }

    public boolean getMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "BaseListingResponse{" +
                "boundaries=" + boundaries +
                ", before=" + before +
                ", after=" + after +
                ", updates=" + updates +
                ", deletes=" + deletes +
                ", more=" + more +
                ", e_tag='" + e_tag + '\'' +
                ", context='" + context + '\'' +
                '}';
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
