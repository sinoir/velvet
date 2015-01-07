package com.delectable.mobile.api.models;

/**
 * The result object from a hashtag search.
 */
public class HashtagResult {

    private String tag;

    //Long because can be null
    private Long capture_count;

    public String getTag() {
        return tag;
    }

    public Long getCaptureCount() {
        return capture_count;
    }

    @Override
    public String toString() {
        return "HashtagResult{" +
                "tag='" + tag + '\'' +
                ", capture_count=" + capture_count +
                '}';
    }
}
