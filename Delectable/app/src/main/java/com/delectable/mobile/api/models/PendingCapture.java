package com.delectable.mobile.api.models;

import java.util.List;

public class PendingCapture {

    private String id;

    private Float created_at;

    private PhotoHash photo;

    private Boolean from_camera_roll;

    private Float capture_latitude;

    private Float capture_longitude;

    private BaseWine base_wine;

    private WineProfile wine_profile;

    private String transcription_error_message;

    private List<Match> matches;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Float created_at) {
        this.created_at = created_at;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public Boolean getFromCameraRoll() {
        return from_camera_roll;
    }

    public void setFromCameraRoll(Boolean from_camera_roll) {
        this.from_camera_roll = from_camera_roll;
    }

    public Float getCaptureLatitude() {
        return capture_latitude;
    }

    public void setCaptureLatitude(Float capture_latitude) {
        this.capture_latitude = capture_latitude;
    }

    public Float getCaptureLongitude() {
        return capture_longitude;
    }

    public void setCaptureLongitude(Float capture_longitude) {
        this.capture_longitude = capture_longitude;
    }

    public BaseWine getBaseWine() {
        return base_wine;
    }

    public void setBaseWine(BaseWine base_wine) {
        this.base_wine = base_wine;
    }

    public WineProfile getWineProfile() {
        return wine_profile;
    }

    public void setWineProfile(WineProfile wine_profile) {
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

        private Float score;

        private BaseWine base_wine;

        public Float getScore() {
            return score;
        }

        public void setScore(Float score) {
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