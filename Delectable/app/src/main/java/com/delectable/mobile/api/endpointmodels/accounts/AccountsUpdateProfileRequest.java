package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class AccountsUpdateProfileRequest extends BaseRequest {

    private Payload payload;

    public AccountsUpdateProfileRequest(String fname, String lname, String url, String bio) {
        this.payload = new Payload(fname, lname, url, bio);
    }

    public static class Payload {

        private String fname;

        private String lname;

        private String url;

        private String bio;

        public Payload(String fname, String lname, String url, String bio) {
            this.fname = fname;
            this.lname = lname;
            this.url = url;
            this.bio = bio;
        }
    }

}
