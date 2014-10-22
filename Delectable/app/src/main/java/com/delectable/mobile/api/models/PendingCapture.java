package com.delectable.mobile.api.models;

import java.util.List;

public class PendingCapture {

    private String id;

    private double created_at;

    private PhotoHash photo;

    private boolean from_camera_roll;

    private double capture_latitude;

    private double capture_longitude;

    private BaseWine base_wine;

    private WineProfileMinimal wine_profile;

    private String transcription_error_message;

    private List<Match> matches;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(double created_at) {
        this.created_at = created_at;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public boolean getFromCameraRoll() {
        return from_camera_roll;
    }

    public void setFromCameraRoll(boolean from_camera_roll) {
        this.from_camera_roll = from_camera_roll;
    }

    public double getCaptureLatitude() {
        return capture_latitude;
    }

    public void setCaptureLatitude(double capture_latitude) {
        this.capture_latitude = capture_latitude;
    }

    public double getCaptureLongitude() {
        return capture_longitude;
    }

    public void setCaptureLongitude(double capture_longitude) {
        this.capture_longitude = capture_longitude;
    }

    public BaseWine getBaseWine() {
        return base_wine;
    }

    public void setBaseWine(BaseWine base_wine) {
        this.base_wine = base_wine;
    }

    public WineProfileMinimal getWineProfile() {
        return wine_profile;
    }

    public void setWineProfile(WineProfileMinimal wine_profile) {
        this.wine_profile = wine_profile;
    }

    public String getTranscriptionErrorMessage() {
        return transcription_error_message;
    }

    public void setTranscriptionErrorMessage(String transcription_error_message) {
        this.transcription_error_message = transcription_error_message;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public static class Match {

        private double score;

        private BaseWine base_wine;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public BaseWine getBase_wine() {
            return base_wine;
        }

        public void setBase_wine(BaseWine base_wine) {
            this.base_wine = base_wine;
        }
    }
}