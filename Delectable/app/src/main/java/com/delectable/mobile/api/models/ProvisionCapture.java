package com.delectable.mobile.api.models;

import java.util.HashMap;

public class ProvisionCapture extends BaseResponse {

    String bucket;

    String filename;

    String url;

    HashMap<String, String> headers;

    public String getBucket() {
        return bucket;
    }

    public String getFilename() {
        return filename;
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
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
