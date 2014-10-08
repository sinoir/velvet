package com.delectable.mobile.model.api.registrations;

import com.delectable.mobile.model.api.BaseRequest;

public class ResetPasswordRequest extends BaseRequest {

    Payload payload;

    public ResetPasswordRequest(Payload payload) {
        this.payload = payload;
    }

    public static class Payload {

        String email;

        public Payload(String email) {
            this.email = email;
        }

    }

}
