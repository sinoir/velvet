package com.delectable.mobile.api.models;

import java.util.List;

public class CaptureFeed {

    private String key;

    private String feed_type;

    private String title;

    private String banner;

    private List<BannerAttributes> banner_attributes;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public List<BannerAttributes> getBannerAttributes() {
        return banner_attributes;
    }

    public void setBannerAttributes(List<BannerAttributes> banner_attributes) {
        this.banner_attributes = banner_attributes;
    }

    @Override
    public String toString() {
        return "CaptureFeed{" +
                "key='" + key + '\'' +
                ", feed_type='" + feed_type + '\'' +
                ", title='" + title + '\'' +
                ", banner='" + banner + '\'' +
                ", banner_attributes=" + banner_attributes +
                '}';
    }

    public static class BannerAttributes {

        private String type;

        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "BannerAttributes{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaptureFeed that = (CaptureFeed) o;

        if (banner != null ? !banner.equals(that.banner) : that.banner != null) {
            return false;
        }
        if (banner_attributes != null ? !banner_attributes.equals(that.banner_attributes)
                : that.banner_attributes != null) {
            return false;
        }
        if (feed_type != null ? !feed_type.equals(that.feed_type) : that.feed_type != null) {
            return false;
        }
        if (!key.equals(that.key)) {
            return false;
        }
        if (title != null ? !title.equals(that.title) : that.title != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + (feed_type != null ? feed_type.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (banner != null ? banner.hashCode() : 0);
        result = 31 * result + (banner_attributes != null ? banner_attributes.hashCode() : 0);
        return result;
    }
}
