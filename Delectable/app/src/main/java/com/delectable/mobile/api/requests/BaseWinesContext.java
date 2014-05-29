package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.BaseWine;

import org.json.JSONObject;

public class BaseWinesContext extends BaseRequest {

    String id;

    public BaseWinesContext() {
        context = "profile";
    }

    @Override
    public String[] getPayloadFields() {
        return new String[] {
                "id",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/base_wines/context";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        BaseWine resForParsing = new BaseWine();
        return resForParsing.buildFromJson(jsonObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}