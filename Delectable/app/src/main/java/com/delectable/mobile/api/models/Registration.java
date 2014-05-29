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

        return newRegistration;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Boolean getNewUser() {
        return new_user;
    }

    public void setNewUser(Boolean new_user) {
        this.new_user = new_user;
    }

    public String getSessionToken() {
        return session_token;
    }

    public void setSessionToken(String session_token) {
        this.session_token = session_token;
    }

    public String getSessionType() {
        return session_type;
    }

    public void setSessionType(String session_type) {
        this.session_type = session_type;
    }

    public String getSessionKey() {
        return session_key;
    }

    public void setSessionKey(String session_key) {
        this.session_key = session_key;
    }
}
