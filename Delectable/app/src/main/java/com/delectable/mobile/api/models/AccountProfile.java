package com.delectable.mobile.api.models;

import java.util.List;

public class AccountProfile extends AccountMinimal{

    private int follower_count;

    private int following_count;

    private int capture_count;

    //does not exist in AccountPrivate subclass
    private int public_capture_count;

    private int region_count;

    private int  wishlist_count;

    private String url;

    //does not exist in AccountPrivate subclass
    private List<CaptureSummary> capture_summaries;

    public int getFollowerCount() {
        return follower_count;
    }

    public void setFollowerCount(int follower_count) {
        this.follower_count = follower_count;
    }

    public int getFollowingCount() {
        return following_count;
    }

    public void setFollowingCount(int following_count) {
        this.following_count = following_count;
    }

    public int getCaptureCount() {
        return capture_count;
    }

    public void setCaptureCount(int capture_count) {
        this.capture_count = capture_count;
    }

    public int getPublicCaptureCount() {
        return public_capture_count;
    }

    public void setPublicCaptureCount(int public_capture_count) {
        this.public_capture_count = public_capture_count;
    }

    public int getRegionCount() {
        return region_count;
    }

    public void setRegionCount(int region_count) {
        this.region_count = region_count;
    }

    public int getWishlistCount() {
        return wishlist_count;
    }

    public void setWishlistCount(int wishlist_count) {
        this.wishlist_count = wishlist_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<CaptureSummary> getCaptureSummaries() {
        return capture_summaries;
    }

    public void setCaptureSummaries(List<CaptureSummary> capture_summaries) {
        this.capture_summaries = capture_summaries;
    }

    @Override
    public String toString() {
        return "AccountProfile{" +
                "id='" + getId() + '\'' +
                ", fname='" + getFname() + '\'' +
                ", lname='" + getLname() + '\'' +
                ", bio='" + getBio() + '\'' +
                ", photo=" + getPhoto() +
                ", influencer=" + isInfluencer() +
                ", influencer_titles=" + getInfluencerTitles() +
                ", context='" + getContext() + '\'' +
                ", current_user_relationship=" + getCurrentUserRelationship() +
                ", shadowbanned=" + isShadowbanned() +
                ", e_tag='" + getETag() + '\'' +
                ", follower_count=" + follower_count +
                ", following_count=" + following_count +
                ", capture_count=" + capture_count +
                ", public_capture_count=" + public_capture_count +
                ", region_count=" + region_count +
                ", wishlist_count=" + wishlist_count +
                ", url='" + url + '\'' +
                ", capture_summaries=" + capture_summaries +
                '}';
    }
}
