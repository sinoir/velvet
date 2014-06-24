package com.delectable.mobile.api.models;

import org.json.JSONObject;

import android.util.Log;

// TODO: Test Class
public class LabelScan extends BaseResponse {

    private static final String TAG = "LabelScan";

    String id;

    String photo;

    BaseWine base_wine;

    @Override
    public LabelScan buildFromJson(JSONObject jsonObj) {
        JSONObject payloadObj = jsonObj.optJSONObject("payload");
        Log.d(TAG, "Label Response: " + jsonObj);
        LabelScan newResource = null;
        if (payloadObj != null && payloadObj.optJSONObject("label_scan") != null) {
            newResource = buildFromJson(payloadObj.optJSONObject("label_scan"), this.getClass());
        }
        return newResource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public BaseWine getBaseWine() {
        return base_wine;
    }

    public void setBaseWine(BaseWine base_wine) {
        this.base_wine = base_wine;
    }
}
