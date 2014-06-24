package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.ProvisionCapture;

public abstract class BaseScanRequest extends BaseRequest {

    String bucket;

    String filename;

    public BaseScanRequest(ProvisionCapture provision) {
        setBucket(provision.getBucket());
        setFilename(provision.getFilename());
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
}
