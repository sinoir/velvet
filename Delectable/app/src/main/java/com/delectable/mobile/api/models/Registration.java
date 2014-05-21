package com.delectable.mobile.api.models;

import com.delectable.mobile.api.Actions;

import org.json.JSONObject;

import android.util.SparseArray;

/**
 * Object used to build / parse payloads for Registering / Logging in Created by abednarek on
 * 5/21/14.
 */
public class Registration extends Resource implements Actions.RegistrationActions {

    private static final String sBaseUri = API_VER + "/registrations";

    private static final SparseArray<String> sActionUris = new SparseArray<String>();

    private static final SparseArray<String[]> sActionPayloadFields = new SparseArray<String[]>();

    static {
        sActionUris.append(A_REGISTER, sBaseUri + "/register");
        sActionUris.append(A_LOGIN, sBaseUri + "/login");
        sActionUris.append(A_FACEBOOK, sBaseUri + "/facebook");

        sActionPayloadFields.append(A_REGISTER, new String[]{
                "session_type",
                "email",
                "password",
                "fname",
                "lname",
        });

        sActionPayloadFields.append(A_LOGIN, new String[]{
                "session_type",
                "email",
                "password",
        });

        sActionPayloadFields.append(A_FACEBOOK, new String[]{
                "session_type",
                "facebook_token",
                "facebook_token_expiration",
        });
    }

    // TODO: Create and Link to Account Object when parsing JSON response
    String account;

    String email;

    String facebook_token;

    Double facebook_token_expiration;

    String fname;

    String lname;

    String password;

    String session_token;

    String session_type;

    String session_key;

    @Override
    public String[] getPayloadFieldsForAction(int action) {
        return sActionPayloadFields.get(action);
    }

    @Override
    public String parsePayloadForAction(JSONObject payload, int action) {
        // TODO: Parse using GSON from JSON Payload
        return null;
    }

    @Override
    public String getResourceUrlForAction(int action) {
        return sActionUris.get(action);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
