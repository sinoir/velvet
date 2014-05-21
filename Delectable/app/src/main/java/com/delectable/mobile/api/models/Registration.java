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

    // TODO: Create and Link to Account Object
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
}
