package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.model.api.BaseRequest;

public class AccountsActivityFeedRequest extends BaseRequest {

    private Payload payload;

    public AccountsActivityFeedRequest(String before, String after) {
        this.payload = new Payload(before, after);
    }

    public static class Payload {

        private String before;

        private String after;

        public Payload(String before, String after) {
            this.before = before;
            this.after = after;
        }
    }
}