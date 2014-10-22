package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.models.TaggeeContact;

import java.util.List;

public class AccountsContactListSuggestionsRequest extends BaseRequest {

    private Payload payload;

    public AccountsContactListSuggestionsRequest(List<TaggeeContact> contacts,
            String userCountryCode) {
        payload = new Payload(contacts, userCountryCode);
    }

    public static class Payload {

        private List<TaggeeContact> contacts;

        private String user_country_code;

        public Payload(List<TaggeeContact> contacts, String user_country_code) {
            this.contacts = contacts;
            this.user_country_code = user_country_code;
        }
    }

}
