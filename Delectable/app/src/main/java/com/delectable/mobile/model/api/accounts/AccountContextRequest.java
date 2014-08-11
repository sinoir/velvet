package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.model.api.BaseRequest;

public class AccountContextRequest extends BaseRequest {

    AccountContextPayload payload;

    public AccountContextRequest(String context, String e_tag, AccountContextPayload payload) {
        super(context, e_tag);
        this.payload = payload;
    }

    public AccountContextRequest(String context, AccountContextPayload payload) {
        this(context, null, payload);
    }

    public static class AccountContextPayload {

        String id;

        public AccountContextPayload(String id) {
            this.id = id;
        }
    }

}
