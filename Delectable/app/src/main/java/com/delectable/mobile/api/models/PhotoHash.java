package com.delectable.mobile.api.models;

import java.util.Map;

public class PhotoHash {

    String url;

    // TODO: Build custom object for each resolution or hash of Ids?
    Map<String, String> child_resolutions;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getChildResolutions() {
        return child_resolutions;
    }

    public void setChildResolutions(Map<String, String> child_resolutions) {
        this.child_resolutions = child_resolutions;
    }
}
