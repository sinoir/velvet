package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.model.api.BaseRequest;

import java.util.List;

public class AccountContactListSuggestionsRequest extends BaseRequest {

    ContactListPayload payload;

    public AccountContactListSuggestionsRequest(List<TaggeeContact> contacts,
            String userCountryCode) {
        this.payload = new ContactListPayload();
        this.payload.contacts = contacts;
        this.payload.user_country_code = userCountryCode;
    }

    public static class ContactListPayload {

        String user_country_code;

        List<TaggeeContact> contacts;
    }

}