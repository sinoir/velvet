package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.model.api.BaseRequest;

public class AccountsFollowersRequest extends BaseRequest {

    private Payload payload;

    public AccountsFollowersRequest(String e_tag, String id, String before, String after) {
        super(null, e_tag);
        payload = new Payload(id, before, after);
    }

    public static class Payload {

        // optional - Assumes current user if not present
        private String id;

        // optional – The first time a user's list is retrieved, this will be empty
        private String before;

        // optional – The first time a user's list is retrieved, this will be empty
        private String after;

        public Payload(String id, String before, String after) {
            this.id = id;
            this.before = before;
            this.after = after;
        }
    }
}
