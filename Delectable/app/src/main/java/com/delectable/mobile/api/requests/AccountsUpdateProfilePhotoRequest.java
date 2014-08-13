package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.ProvisionCapture;

import org.json.JSONObject;

public class AccountsUpdateProfilePhotoRequest extends BaseRequest {


    public static final String TAG = AccountsUpdateProfilePhotoRequest.class.getSimpleName();

    private String bucket;

    private String filename;

    public AccountsUpdateProfilePhotoRequest(String bucket, String filename) {
        this.bucket = bucket;
        this.filename = filename;
    }

    @Override
    public String[] getPayloadFields() {
        String[] payload = {
                "bucket",
                "filename"
        };
        return payload;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/update_profile_photo";
    }

    @Override
    public PhotoHash buildResopnseFromJson(JSONObject jsonObject) {
        return PhotoHash.buildFromJson(jsonObject);
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
