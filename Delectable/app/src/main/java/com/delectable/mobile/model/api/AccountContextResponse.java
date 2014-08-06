package com.delectable.mobile.model.api;

import com.delectable.mobile.model.local.Account;

public class AccountContextResponse extends BaseResponse {

    public AccountContextResponsePayload payload;

    public static class AccountContextResponsePayload {
        public Account account;
    }

}
