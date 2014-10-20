package com.delectable.mobile.api.endpointmodels.registrations;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.endpointmodels.BaseResponse;

public class RegistrationLoginResponse extends BaseResponse {

    public RegistrationLoginResponsePayload payload;

    public static class RegistrationLoginResponsePayload {

        public String session_key;

        public String session_token;

        public Account account;
    }

}
