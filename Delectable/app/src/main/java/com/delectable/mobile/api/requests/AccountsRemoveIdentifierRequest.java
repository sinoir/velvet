package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.IdentifiersListing;

import org.json.JSONObject;

/**
 * Used to remove a phone number from an account.
 */
public class AccountsRemoveIdentifierRequest extends BaseRequest{

    private static final String TAG = AccountsRemoveIdentifierRequest.class.getSimpleName();

    private String identifier_id;

    public AccountsRemoveIdentifierRequest(Identifier identifier) {
        this.identifier_id = identifier.getId();
    }
    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "identifier_id"
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/remove_identifier";
    }

    @Override
    public IdentifiersListing buildResopnseFromJson(JSONObject jsonObject) {
        return IdentifiersListing.buildFromJson(jsonObject);
    }

    public String getIdentifierId() {
        return identifier_id;
    }

    public void setIdentifierId(String identifier_id) {
        this.identifier_id = identifier_id;
    }

}
