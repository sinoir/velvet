package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.model.api.BaseResponse;

/**
 * These endpoints have this response:
 * /accounts/update_profile_photo
 * /accounts/facebookify_profile_photo
 */
public class AccountsPhotoHashResponse extends BaseResponse {

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
