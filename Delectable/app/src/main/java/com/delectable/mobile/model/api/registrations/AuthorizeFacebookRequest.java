package com.delectable.mobile.model.api.registrations;

import com.delectable.mobile.Config;
import com.delectable.mobile.model.api.BaseRequest;

public class AuthorizeFacebookRequest extends BaseRequest {

    private Payload payload;

    /**
     * Makes a request object with just the facebook_token and facebook_token_expiration fields set.
     */
    public AuthorizeFacebookRequest(String facebook_token, double facebook_token_expiration) {
        this(null, null, facebook_token, facebook_token_expiration);
    }

    /**
     * Makes a request object with the payload session_type set to the {@link
     * Config#DEFAULT_SESSION_TYPE}
     */
    public AuthorizeFacebookRequest(String device_id, String facebook_token,
            double facebook_token_expiration) {
        this(Config.DEFAULT_SESSION_TYPE, device_id, facebook_token, facebook_token_expiration);
    }

    public AuthorizeFacebookRequest(String session_type, String device_id, String facebook_token,
            double facebook_token_expiration) {
        payload = new Payload(session_type, device_id, facebook_token, facebook_token_expiration);
    }

    public static class Payload {

        private String session_type;

        private String device_id;

        private String facebook_token;

        private double facebook_token_expiration;

        public Payload(String session_type, String device_id, String facebook_token,
                double facebook_token_expiration) {
            this.session_type = session_type;
            this.device_id = device_id;
            this.facebook_token = facebook_token;
            this.facebook_token_expiration = facebook_token_expiration;
        }
    }

}
