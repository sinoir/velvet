package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.model.api.BaseRequest;

public class AccountsAddIdentifierRequest extends BaseRequest {

    private Payload payload;

    public AccountsAddIdentifierRequest(String string, String type) {
        this(string, type, null);
    }

    public AccountsAddIdentifierRequest(String string, String type, String userCountryCode) {
        this.payload = new Payload(string, type, userCountryCode);
    }

    public static class Payload {

        private String string;

        private String type;

        private String user_country_code;

        public Payload(String string, String type, String user_country_code) {
            this.string = string;
            this.type = type;
            this.user_country_code = user_country_code;
        }
    }
}
