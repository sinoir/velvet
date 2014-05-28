package com.delectable.mobile.api.requests;

public class RegistrationsFacebook extends BaseRegistrations {

    String facebook_token;

    Double facebook_token_expiration;

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "session_type",
                "facebook_token",
                "facebook_token_expiration",
        };
    }

    @Override
    public String getResourceUrl() {
        return sBaseUri + "/register";
    }

    public String getFacebookToken() {
        return facebook_token;
    }

    public void setFacebookToken(String facebook_token) {
        this.facebook_token = facebook_token;
    }

    public Double getFacebookTokenExpiration() {
        return facebook_token_expiration;
    }

    public void setFacebookTokenExpiration(Double facebook_token_expiration) {
        this.facebook_token_expiration = facebook_token_expiration;
    }
}
