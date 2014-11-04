package com.delectable.mobile.api.endpointmodels.registrations;

import com.delectable.mobile.Config;
import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class RegistrationsLoginRequest extends BaseRequest {

    private Payload payload;

    public RegistrationsLoginRequest(String device_id, String email,
            String password) {
        this.payload = new Payload(device_id, email, password);
    }

    public static class Payload {

        private String session_type;

        private String device_id;

        private String email;

        private String password;

        public Payload(String deviceId, String email, String password) {
            this(Config.DEFAULT_SESSION_TYPE, deviceId, email, password);
        }

        public Payload(String session_type, String device_id, String email,
                String password) {
            this.session_type = session_type;
            this.device_id = device_id;
            this.email = email;
            this.password = password;
        }

    }

}
