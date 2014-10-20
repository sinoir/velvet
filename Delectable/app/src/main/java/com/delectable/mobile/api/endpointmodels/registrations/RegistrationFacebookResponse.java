package com.delectable.mobile.api.endpointmodels.registrations;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.Account;

public class RegistrationFacebookResponse extends BaseResponse {

    public RegistrationFacebookResponsePayload payload;

    public static class RegistrationFacebookResponsePayload {

        public String session_key;

        public String session_token;

        public boolean new_user;

        public Account account;
    }

}
