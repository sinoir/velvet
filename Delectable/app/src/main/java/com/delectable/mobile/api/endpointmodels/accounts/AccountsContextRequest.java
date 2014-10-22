package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class AccountsContextRequest extends BaseRequest {

    private Payload payload;

    public AccountsContextRequest(AccountContext context, String id) {
        this(context, null, id);
    }

    public AccountsContextRequest(AccountContext context, String e_tag, String id) {
        super(context.toString(), e_tag);
        this.payload = new Payload(id);
    }

    public static class Payload {

        private String id;

        public Payload(String id) {
            this.id = id;
        }
    }

}
