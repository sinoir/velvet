package com.delectable.mobile.model.api.scanwinelabels;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.model.api.BaseRequest;

public class PhotoUploadRequest extends BaseRequest {

    private Payload payload = new Payload();

    public PhotoUploadRequest(ProvisionCapture provision) {
        this.payload.bucket = provision.getBucket();
        this.payload.filename = provision.getFilename();
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public static class Payload {

        private String bucket;

        private String filename;

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

}
