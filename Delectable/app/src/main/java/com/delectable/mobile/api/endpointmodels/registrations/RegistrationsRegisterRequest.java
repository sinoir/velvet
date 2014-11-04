package com.delectable.mobile.api.endpointmodels.registrations;

import com.delectable.mobile.Config;
import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class RegistrationsRegisterRequest extends BaseRequest {

    private Payload payload;

    public RegistrationsRegisterRequest(String device_id, String email,
            String password, String fname, String lname) {
        payload = new Payload(device_id, email, password, fname, lname);
    }

    public static class Payload {

        private String session_type;

        private String device_id;

        private String email;

        private String password;

        private String fname;

        private String lname;

        public Payload(String device_id, String email, String password, String fname, String lname) {
            this(Config.DEFAULT_SESSION_TYPE, device_id, email, password, fname, lname);
        }

        public Payload(String session_type, String device_id, String email,
                String password, String fname, String lname) {
            this.session_type = session_type;
            this.device_id = device_id;
            this.email = email;
            this.password = password;
            this.fname = fname;
            this.lname = lname;
        }
    }
}
