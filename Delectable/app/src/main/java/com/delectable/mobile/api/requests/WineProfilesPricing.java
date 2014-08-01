package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.WineSource;

import org.json.JSONObject;

public class WineProfilesPricing extends BaseRequest {

    String id;

    String state;

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
                "state",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/wine_profiles/source";
    }

    @Override
    public WineSource buildResopnseFromJson(JSONObject jsonObject) {
        return WineSource.buildFromJson(jsonObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    /**
     * Set State as in US State / Province.
     */
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "WineProfilesPricing{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
