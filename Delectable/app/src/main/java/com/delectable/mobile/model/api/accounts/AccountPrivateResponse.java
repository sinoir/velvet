package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.model.api.BaseResponse;

public class AccountPrivateResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private Account account;

        public Account getAccount() {
            return account;
        }
    }

}
