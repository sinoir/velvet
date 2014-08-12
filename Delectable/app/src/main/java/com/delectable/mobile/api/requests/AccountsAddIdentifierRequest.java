package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.IdentifiersListing;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Used to remove a phone number from an account.
 */
public class AccountsAddIdentifierRequest extends BaseRequest {

    private static final String TAG = AccountsAddIdentifierRequest.class.getSimpleName();

    private String string;

    private String type;

    /**
     * optional field
     */
    private String user_country_code;


    public AccountsAddIdentifierRequest(String string, String type) {
        this.string = string;
        this.type = type;
    }

    @Override
    public String[] getPayloadFields() {
        ArrayList<String> payload = new ArrayList<String>();
        payload.add("string");
        payload.add("type");
        if (user_country_code != null) {
            payload.add("user_country_code");
        }
        return payload.toArray(new String[payload.size()]);
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/add_identifier";
    }

    @Override
    public IdentifiersListing buildResopnseFromJson(JSONObject jsonObject) {
        return IdentifiersListing.buildFromJson(jsonObject);
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserCountryCode() {
        return user_country_code;
    }

    public void setUserCountryCode(String user_country_code) {
        this.user_country_code = user_country_code;
    }

}
