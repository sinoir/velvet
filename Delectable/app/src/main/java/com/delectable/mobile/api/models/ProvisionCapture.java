package com.delectable.mobile.api.models;

import java.util.HashMap;

public class ProvisionCapture extends BaseResponse {

    private String bucket;

    private String filename;

    private String url;

    private HashMap<String, String> headers;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "ProvisionCapture{" +
                "bucket='" + bucket + '\'' +
                ", filename='" + filename + '\'' +
                ", headers=" + headers +
                '}';
    }
}
