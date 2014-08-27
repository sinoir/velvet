package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.model.api.BaseRequest;

public class AccountsRemoveIdentifierRequest extends BaseRequest {

    private Payload payload;

    public AccountsRemoveIdentifierRequest(String identifier_id) {
        this.payload = new Payload(identifier_id);
    }

    public static class Payload {

        private String identifier_id;

        public Payload(String identifier_id) {
            this.identifier_id = identifier_id;
        }
    }
}
