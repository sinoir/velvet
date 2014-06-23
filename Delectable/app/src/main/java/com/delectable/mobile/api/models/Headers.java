package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

public class Headers {

    @SerializedName("Content-Type")
    String content_type;

    String url;

    @SerializedName("Cache-Control")
    String cache_control;

    @SerializedName("Authorization")
    String authorization;

    String date;

    public String getContentType() {
        return content_type;
    }

    public void setContentType(String content_type) {
        this.content_type = content_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCacheControl() {
        return cache_control;
    }

    public void setCacheControl(String cache_control) {
        this.cache_control = cache_control;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Headers{" +
                "content_type='" + content_type + '\'' +
                ", url='" + url + '\'' +
                ", cache_control='" + cache_control + '\'' +
                ", authorization='" + authorization + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
