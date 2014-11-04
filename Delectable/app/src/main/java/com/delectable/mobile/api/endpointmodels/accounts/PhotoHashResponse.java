package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.endpointmodels.BaseResponse;

public class PhotoHashResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private PhotoHash photo;

        public PhotoHash getPhoto() {
            return photo;
        }
    }
}
