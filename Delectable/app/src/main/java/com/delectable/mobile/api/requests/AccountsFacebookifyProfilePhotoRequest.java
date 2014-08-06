package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.ProvisionCapture;

import org.json.JSONObject;

public class AccountsFacebookifyProfilePhotoRequest extends BaseRequest {

    public static String TAG = AccountsFacebookifyProfilePhotoRequest.class.getSimpleName();

    @Override
    public String[] getPayloadFields() {
        //build empty payload
        return new String[0];
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/facebookify_profile_photo";
    }

    @Override
    public PhotoHash buildResopnseFromJson(JSONObject jsonObject) {
        return PhotoHash.buildFromJson(jsonObject);
    }
}
