package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class BaseActionRequest extends BaseRequest {

    private static final String TAG = "BaseActionRequest";

    String id;

    Boolean action;


    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
        };
    }

    @Override
    public JSONObject buildPayload() {
        JSONObject jsonObject = super.buildPayload();
        if (action != null) {
            try {
                jsonObject.putOpt("action", action.booleanValue());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.wtf(TAG, "Failed to build payload", e);
            }
        }
        return jsonObject;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/like";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        // No Response Payload
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

}
