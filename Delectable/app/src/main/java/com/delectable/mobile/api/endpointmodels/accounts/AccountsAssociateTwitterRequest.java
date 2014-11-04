package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class AccountsAssociateTwitterRequest extends BaseRequest {

    private Payload payload;

    public AccountsAssociateTwitterRequest(long twitter_id, String token, String token_secret,
            String screen_name) {
        payload = new Payload(twitter_id, token, token_secret, screen_name);
    }

    public static class Payload {

        private long twitter_id;

        private String token;

        private String token_secret;

        private String screen_name;

        public Payload(long twitter_id, String token, String token_secret, String screen_name) {
            this.twitter_id = twitter_id;
            this.token = token;
            this.token_secret = token_secret;
            this.screen_name = screen_name;
        }
    }

}
