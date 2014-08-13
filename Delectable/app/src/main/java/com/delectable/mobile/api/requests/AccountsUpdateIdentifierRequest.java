package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.IdentifiersListing;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Used to remove a phone number from an account.
 */
public class AccountsUpdateIdentifierRequest extends BaseRequest {

    private static final String TAG = AccountsUpdateIdentifierRequest.class.getSimpleName();

    private String identifierId;

    private String string;

    /**
     * optional field
     */
    private String user_country_code;

    public AccountsUpdateIdentifierRequest(Identifier identifier, String replacement) {
        this.identifierId = identifier.getId();
        this.string = replacement;
    }

    @Override
    public String[] getPayloadFields() {
        ArrayList<String> payload = new ArrayList<String>();
        payload.add("identifier_id");
        payload.add("string");
        if (user_country_code != null) {
            payload.add("user_country_code");
        }
        return payload.toArray(new String[payload.size()]);
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/update_identifier";
    }

    @Override
    public IdentifiersListing buildResopnseFromJson(JSONObject jsonObject) {
        return IdentifiersListing.buildFromJson(jsonObject);
    }

    public String getIdentifierId() {
        return identifierId;
    }

    public String getString() {
        return string;
    }

    public String getUserCountryCode() {
        return user_country_code;
    }

    public void setUserCountryCode(String user_country_code) {
        this.user_country_code = user_country_code;
    }

}
