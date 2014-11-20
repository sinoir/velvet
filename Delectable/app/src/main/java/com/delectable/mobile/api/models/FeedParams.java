package com.delectable.mobile.api.models;

import java.util.List;

public class FeedParams {

    private String key;

    private String id;

    private String feed_type;

    private String title;

    private String banner;

    private List<CaptureFeed.BannerAttributes> banner_attributes;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedType() {
        return feed_type;
    }

    public void setFeedType(String feed_type) {
        this.feed_type = feed_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<CaptureFeed.BannerAttributes> getBannerAttributes() {
        return banner_attributes;
    }

    public void setBannerAttributes(List<CaptureFeed.BannerAttributes> banner_attributes) {
        this.banner_attributes = banner_attributes;
    }

    @Override
    public String toString() {
        return "FeedParams{" +
                "key='" + key + '\'' +
                ", id='" + id + '\'' +
                ", feed_type='" + feed_type + '\'' +
                ", title='" + title + '\'' +
                ", banner='" + banner + '\'' +
                ", banner_attributes=" + banner_attributes +
                '}';
    }
}
