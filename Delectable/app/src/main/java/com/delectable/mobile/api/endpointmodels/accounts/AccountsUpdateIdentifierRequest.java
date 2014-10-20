package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class AccountsUpdateIdentifierRequest extends BaseRequest {

    private Payload payload;

    public AccountsUpdateIdentifierRequest(String identifierId, String string) {
        this(identifierId, string, null);
    }

    public AccountsUpdateIdentifierRequest(String identifierId, String string,
            String userCountryCode) {
        this.payload = new Payload(identifierId, string, userCountryCode);
    }

    public static class Payload {

        private String identifier_id;

        private String string;

        private String user_country_code;

        public Payload(String identifier_id, String string, String user_country_code) {
            this.identifier_id = identifier_id;
            this.string = string;
            this.user_country_code = user_country_code;
        }
    }
}
