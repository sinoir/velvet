package com.delectable.mobile.api.models;

import java.util.List;

public class PendingCapture extends BaseListingElement {

    private PhotoHash photo;

    private boolean from_camera_roll;

    private double capture_latitude;

    private double capture_longitude;

    private WineProfileMinimal wine_profile;

    private BaseWine base_wine;

    private String transcription_error_message;

    private List<Match> matches;

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public boolean isFromCameraRoll() {
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

    @Override
    public String toString() {
        return "PendingCapture{" +
                "id='" + getId() + '\'' +
                ", created_at=" + getCreatedAt() +
                "photo=" + photo +
                ", from_camera_roll=" + from_camera_roll +
                ", capture_latitude=" + capture_latitude +
                ", capture_longitude=" + capture_longitude +
                ", wine_profile=" + wine_profile +
                ", base_wine=" + base_wine +
                ", transcription_error_message='" + transcription_error_message + '\'' +
                ", matches=" + matches +
                ", context='" + getContext() + '\'' +
                ", e_tag='" + getETag() + '\'' +
                ", listing_params=" + getListParams() +
                '}';
    }
}