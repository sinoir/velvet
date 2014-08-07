package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.model.api.BaseResponse;
import com.delectable.mobile.model.local.Account;

public class AccountContextResponse extends BaseResponse {

    public AccountContextResponsePayload payload;

    public static class AccountContextResponsePayload {

        public Account account;
    }

}
