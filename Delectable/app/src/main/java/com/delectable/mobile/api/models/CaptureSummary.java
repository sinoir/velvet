package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class CaptureSummary {

    String title;

    String more_title;

    String type;

    ArrayList<CaptureDetails> captures;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoreTitle() {
        return more_title;
    }

    public void setMoreTitle(String more_title) {
        this.more_title = more_title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<CaptureDetails> getCaptures() {
        return captures;
    }

    public void setCaptures(ArrayList<CaptureDetails> captures) {
        this.captures = captures;
    }

    @Override
    public String toString() {
        return "CaptureSummaries{" +
                "title='" + title + '\'' +
                ", more_title='" + more_title + '\'' +
                ", type='" + type + '\'' +
                ", captures=" + captures +
                '}';
    }
}
