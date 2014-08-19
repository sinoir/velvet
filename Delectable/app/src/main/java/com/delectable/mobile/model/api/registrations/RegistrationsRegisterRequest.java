package com.delectable.mobile.model.api.registrations;

import com.delectable.mobile.Config;
import com.delectable.mobile.model.api.BaseRequest;

public class RegistrationsRegisterRequest extends BaseRequest {

    private Payload payload;

    public RegistrationsRegisterRequest(Payload payload) {
        this.payload = payload;
    }

    public static class Payload {

        private String session_type;

        private String device_id;

        private String email;

        private String password;

        private String fname;

        private String lname;

        public Payload(String email, String password, String fname, String lname) {
            //TODO abstract device_id and pass appropriately
            this(Config.DEFAULT_SESSION_TYPE, null, email, password, fname, lname);
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
