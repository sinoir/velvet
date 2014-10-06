package com.delectable.mobile.api.models;

import java.util.HashMap;

public class CaptureMinimal extends Capture {

    public static int MAX_RATING_VALUE = 40;

    private String short_share_url;

    private String tweet;

    private HashMap<String, Integer> ratings;

    private PhotoHash photo;

    private BaseWine base_wine;

    private WineProfileMinimal wine_profile; //minimal

    /**
     * Updates existing capture with updated capture
     *
     * @param newCapture - Capture must be the same "object" / ID as this, and context must be
     *                   detailed
     */
    public void updateWithNewCapture(CaptureMinimal newCapture) {
        setPrivate(newCapture.getPrivate());
        short_share_url = newCapture.getShortShareUrl();
        tweet = newCapture.getTweet();
        ratings = newCapture.getRatings();
        photo = newCapture.getPhoto();
        base_wine = newCapture.getBaseWine();
        wine_profile = newCapture.getWineProfile();
        setETag(newCapture.getETag());
    }

    /**
     * Get % of Rating
     *
     * @param id = User ID linked to Ratings Hash
     * @return -1.0f if no rating exists, or value between 0.0f and 1.0f
     */
    public float getRatingPercentForId(String id) {
        float ratingPercent = -1.0f;
        ratingPercent = (float) getRatingForId(id) / MAX_RATING_VALUE;
        if (ratingPercent <= 0.0f) {
            ratingPercent = -1.0f;
        }
        return ratingPercent;
    }

    public int getRatingForId(String id) {
        int rating = -1;
        if (ratings != null && ratings.containsKey(id)) {
            rating = ratings.get(id);
        }
        return rating;
    }

    public void updateRatingForUser(String id, int rating) {
        ratings.put(id, rating);
    }


    public String getShortShareUrl() {
        return short_share_url;
    }

    public void setShortShareUrl(String short_share_url) {
        this.short_share_url = short_share_url;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public HashMap<String, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, Integer> ratings) {
        this.ratings = ratings;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
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


    @Override
    public String toString() {
        return "CaptureDetails{" +
                "id='" + getId() + '\'' +
                ", created_at=" + getCreatedAt() +
                ", private_=" + getPrivate() +
                ", context='" + getContext() + '\'' +
                ", e_tag='" + getETag() + '\'' +
                ", short_share_url='" + short_share_url + '\'' +
                ", tweet='" + tweet + '\'' +
                ", ratings=" + ratings +
                ", photo=" + photo +
                ", base_wine=" + base_wine +
                ", wine_profile=" + wine_profile +
                '}';
    }
}
