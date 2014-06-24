package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.ProvisionCapture;

import org.json.JSONObject;

public class ProvisionPhotoUploadRequest extends BaseRequest {

    @Override
    public String[] getPayloadFields() {
        return new String[0];
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/label_scans/provision_photo_upload";
    }

    @Override
    public ProvisionCapture buildResopnseFromJson(JSONObject jsonObject) {
        ProvisionCapture resForParsing = new ProvisionCapture();
        return resForParsing.buildFromJson(jsonObject);
    }
}
