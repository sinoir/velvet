package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.AccountProfile;

public class AccountProfileResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private AccountProfile account;

        public AccountProfile getAccount() {
            return account;
        }
    }

}
