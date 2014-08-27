package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.model.api.BaseRequest;

public class AccountsUpdateProfilePhotoRequest extends BaseRequest {

    private Payload payload;

    public AccountsUpdateProfilePhotoRequest(ProvisionCapture provisionCapture) {
        this(provisionCapture.getBucket(), provisionCapture.getFilename());
    }

    /**
     * @param bucket obtained from a successful photo provision.
     * @param filename obtained from a successful photo provision.
     */
    public AccountsUpdateProfilePhotoRequest(String bucket, String filename) {
        this.payload = new Payload(bucket, filename);
    }

    public static class Payload {

        private String bucket;

        private String filename;

        public Payload(String bucket, String filename) {
            this.bucket = bucket;
            this.filename = filename;
        }
    }

}
