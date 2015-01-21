package com.delectable.mobile.api.models;


import java.util.ArrayList;

public class CaptureNote extends Capture {

    public static int MAX_RATING_VALUE = 40;

    private int capturer_rating;

    private String note;

    ArrayList<CaptureCommentAttributes> comment_attributes;

    private String vintage;

    private ArrayList<String> helpfuling_account_ids;

    private AccountMinimal capturer_participant;

    public float getRatingPercent() {
        float ratingPercent = -1.0f;
        ratingPercent = (float) capturer_rating / MAX_RATING_VALUE;
        if (ratingPercent <= 0.0f) {
            ratingPercent = -1.0f;
        }
        return ratingPercent;
    }

    public int getCapturerRating() {
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

    public void markHelpful(String userId) {
        helpfuling_account_ids.add(userId);
    }

    public void unmarkHelpful(String userId) {
        helpfuling_account_ids.remove(userId);
    }

    public AccountMinimal getCapturerParticipant() {
        return capturer_participant;
    }

    public void setCapturerParticipant(AccountMinimal capturer_participant) {
        this.capturer_participant = capturer_participant;
    }

    public ArrayList<CaptureCommentAttributes> getCommentAttributes() {
        return comment_attributes;
    }

    public void setCommentAttributes(ArrayList<CaptureCommentAttributes> comment_attributes) {
        this.comment_attributes = comment_attributes;
    }

    @Override
    public String toString() {
        return "CaptureNote{" +
                "capturer_rating=" + capturer_rating +
                ", note='" + note + '\'' +
                ", comment_attributes=" + comment_attributes +
                ", vintage='" + vintage + '\'' +
                ", helpfuling_account_ids=" + helpfuling_account_ids +
                ", capturer_participant=" + capturer_participant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaptureNote that = (CaptureNote) o;

        if (!getId().equals(that.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
