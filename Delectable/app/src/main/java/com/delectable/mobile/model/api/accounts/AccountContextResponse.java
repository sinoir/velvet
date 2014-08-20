package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.model.api.BaseResponse;

public class AccountContextResponse extends BaseResponse {

    public AccountContextResponsePayload payload;

    public static class AccountContextResponsePayload {

        public Account account;
    }

}
