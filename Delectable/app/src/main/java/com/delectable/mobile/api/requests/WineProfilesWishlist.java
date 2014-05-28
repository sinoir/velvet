package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;

import org.json.JSONObject;

public class WineProfilesWishlist extends BaseRequest {

    String id;

    Boolean action;

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
                "action"
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/wine_profiles/wishlist";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        // No Payload Response
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
