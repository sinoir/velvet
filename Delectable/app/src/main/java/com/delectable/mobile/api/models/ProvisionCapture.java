package com.delectable.mobile.api.models;

import org.json.JSONObject;

public class ProvisionCapture extends BaseResponse {

    String bucket;

    String filename;

    Headers headers;

    @Override
    public ProvisionCapture buildFromJson(JSONObject jsonObj) {
        JSONObject payloadObj = jsonObj.optJSONObject("payload");
        ProvisionCapture newResource = null;
        if (payloadObj != null) {
            newResource = buildFromJson(payloadObj, this.getClass());
        }

        return newResource;
    }

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

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "ProvisionCapture{" +
                "bucket='" + bucket + '\'' +
                ", filename='" + filename + '\'' +
                ", headers=" + headers +
                "} " + super.toString();
    }
}
