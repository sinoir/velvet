package com.delectable.mobile.api.models;

import org.json.JSONObject;

public class Registration extends BaseResponse {

    Account account;

    Boolean new_user;

    String session_token;

    String session_type;

    String session_key;

    @Override
    public BaseResponse buildFromJson(JSONObject payload) {
        Registration newRegistration = buildFromJson(payload.optJSONObject("payload"),
                this.getClass());

        // Custom Account parsing
        if (payload.optJSONObject("payload") != null) {
            newRegistration
                    .setAccount(Account.parsePrivateAccount(
                            payload.optJSONObject("payload").optJSONObject(
                                    "account")
                    ));
        }

        return newRegistration;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Boolean getNew_user() {
        return new_user;
    }

    public void setNew_user(Boolean new_user) {
        this.new_user = new_user;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getSession_type() {
        return session_type;
    }

    public void setSession_type(String session_type) {
        this.session_type = session_type;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }
}
