package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.BaseResponse;

import org.json.JSONObject;

import java.util.ArrayList;

public class AccountsContextRequest extends BaseRequest {

    public static final String CONTEXT_PROFILE = "profile";
    public static final String CONTEXT_PRIVATE = "private";

    String id;

    /**
     * Make request to get Account details
     * @param contextType - Must be either Profile or Private
     */
    public AccountsContextRequest(String contextType) {
        context = contextType;
    }

    @Override
    public String[] getPayloadFields() {
        // Build Payload Fields according to what is set and not set
        ArrayList<String> fieldsList = new ArrayList<String>();
        if (id != null) {
            fieldsList.add("id");
        }
        String[] fieldsArray = fieldsList.toArray(new String[fieldsList.size()]);
        return fieldsArray;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/context";
    }

    @Override
    public Account buildResopnseFromJson(JSONObject jsonObject) {
        return Account.buildFromJson(jsonObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccountsContextRequest{" +
                "id='" + id + '\'' +
                "} " + super.toString();
    }
}
