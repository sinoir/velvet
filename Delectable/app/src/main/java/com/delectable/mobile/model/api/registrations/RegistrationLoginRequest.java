package com.delectable.mobile.model.api.registrations;

import com.delectable.mobile.Config;
import com.delectable.mobile.model.api.BaseRequest;

public class RegistrationLoginRequest extends BaseRequest {

    RegistrationLoginPayload payload;

    public RegistrationLoginRequest(RegistrationLoginPayload payload) {
        this.payload = payload;
    }

    public static class RegistrationLoginPayload {

        String session_type;

        String device_id;

        String email;

        String password;

        public RegistrationLoginPayload(String email, String password) {
            this(Config.DEFAULT_SESSION_TYPE, null, email, password);

        }

        public RegistrationLoginPayload(String session_type, String device_id, String email,
                String password) {
            this.session_type = session_type;
            this.device_id = device_id;
            this.email = email;
            this.password = password;
        }

    }

}
