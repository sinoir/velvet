package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.ProvisionCapture;

import org.json.JSONObject;

import java.util.ArrayList;

public class AccountsProvisionProfilePhotoRequest extends BaseRequest {

    public static final String TAG = AccountsProvisionProfilePhotoRequest.class.getSimpleName();

    @Override
    public String[] getPayloadFields() {
        //build empty payload
        return new String[0];
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/provision_profile_photo";
    }

    @Override
    public ProvisionCapture buildResopnseFromJson(JSONObject jsonObject) {
        return ProvisionCapture.buildFromJson(jsonObject);
    }
}
