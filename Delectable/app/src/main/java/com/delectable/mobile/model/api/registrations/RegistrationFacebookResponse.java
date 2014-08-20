package com.delectable.mobile.model.api.registrations;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.model.api.BaseResponse;

public class RegistrationFacebookResponse extends BaseResponse {

    public RegistrationFacebookResponsePayload payload;

    public static class RegistrationFacebookResponsePayload {

        public String session_key;

        public String session_token;

        public boolean new_user;

        public Account account;
    }

}
