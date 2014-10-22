package com.delectable.mobile.api.endpointmodels.registrations;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class ResetPasswordRequest extends BaseRequest {

    private Payload payload;

    public ResetPasswordRequest(String email) {
        payload = new Payload(email);
    }

    public static class Payload {

        private String email;

        public Payload(String email) {
            this.email = email;
        }

    }

}
