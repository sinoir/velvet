package com.delectable.mobile.model.api.registrations;

import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.local.Account;

public class RegistrationLoginResponse extends BaseResponse {

    public RegistrationLoginResponsePayload payload;

    public static class RegistrationLoginResponsePayload {

        public String session_key;

        public String session_token;

        public Account account;
    }

}
