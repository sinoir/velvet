package com.delectable.mobile.api.endpointmodels.registrations;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

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
