package com.delectable.mobile.api.endpointmodels.registrations;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.Account;

public class RegistrationsFacebookResponse extends BaseResponse {

    public Payload payload;

    public static class Payload {

        public String session_key;

        public String session_token;

        public boolean new_user;

        public Account account;
    }

}
