package com.delectable.mobile.model.api.registrations;

import com.delectable.mobile.Config;
import com.delectable.mobile.model.api.BaseRequest;

public class RegistrationFacebookRequest extends BaseRequest {

    RegistrationFacebookPayload payload;

    public RegistrationFacebookRequest(RegistrationFacebookPayload payload) {
        this.payload = payload;
    }

    public static class RegistrationFacebookPayload {

        String session_type;

        String device_id;

        String facebook_token;

        double facebook_token_expiration;

        public RegistrationFacebookPayload(String facebook_token,
                double facebook_token_expiration) {
            this(Config.DEFAULT_SESSION_TYPE, null, facebook_token, facebook_token_expiration);

        }

        public RegistrationFacebookPayload(String session_type, String device_id,
                String facebook_token,
                double facebook_token_expiration) {
            this.session_type = session_type;
            this.device_id = device_id;
            this.facebook_token = facebook_token;
            this.facebook_token_expiration = facebook_token_expiration;
        }

    }

}
