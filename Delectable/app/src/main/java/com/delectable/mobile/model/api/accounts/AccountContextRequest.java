package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.model.api.BaseRequest;

public class AccountContextRequest extends BaseRequest {

    private Payload payload;

    public AccountContextRequest(String context, String id) {
        this(context, null, id);
    }

    public AccountContextRequest(String context, String e_tag, String id) {
        super(context, e_tag);
        this.payload = new Payload(id);
    }

    public static class Payload {

        private String id;

        public Payload(String id) {
            this.id = id;
        }
    }

}
