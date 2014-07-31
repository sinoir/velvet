package com.delectable.mobile.api.models;


import java.util.ArrayList;

public class CaptureNote extends Capture {

    public static int MAX_RATING_VALUE = 40;

    Integer capturer_rating;

    String note;

    String vintage;

    ArrayList<String> helpfuling_account_ids;

    public float getRatingPercent() {
        float ratingPercent = -1.0f;
        ratingPercent = (float) capturer_rating / MAX_RATING_VALUE;
        if (ratingPercent <= 0.0f) {
            ratingPercent = -1.0f;
        }
        return ratingPercent;
    }

    public Integer getCapturerRating() {
        return capturer_rating;
    }

    public void setCapturerRating(Integer capturer_rating) {
        this.capturer_rating = capturer_rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public ArrayList<String> getHelpfulingAccountIds() {
        return helpfuling_account_ids;
    }

    public void setHelpfulingAccountIds(ArrayList<String> helpfuling_account_ids) {
        this.helpfuling_account_ids = helpfuling_account_ids;
    }

    @Override
    public String toString() {
        return "CaptureNote{" +
                "capturer_rating=" + capturer_rating +
                ", note='" + note + '\'' +
                ", vintage='" + vintage + '\'' +
                ", helpfuling_account_ids=" + helpfuling_account_ids +
                "} " + super.toString();
    }
}
