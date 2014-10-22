package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class AccountContextRequest extends BaseRequest {

    private Payload payload;

    public AccountContextRequest(AccountContext context, String id) {
        this(context, null, id);
    }

    public AccountContextRequest(AccountContext context, String e_tag, String id) {
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
