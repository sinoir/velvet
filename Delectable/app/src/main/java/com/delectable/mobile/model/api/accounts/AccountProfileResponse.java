package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.model.api.BaseResponse;

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
