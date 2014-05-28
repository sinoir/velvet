package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.WineProfile;

import org.json.JSONObject;

public class WineProfilesContext extends BaseRequest {

    public static final String CONTEXT_TYPE_MINIMAL = "minimal";

    public static final String CONTEXT_TYPE_SUBPROFILE = "subprofile";

    String id;

    public WineProfilesContext() {
        // Context can be either minimal or subprofile
        // TODO: pass appropriate context via params? update when implementing
        context = CONTEXT_TYPE_SUBPROFILE;
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id"
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/wine_profiles/context";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        WineProfile resForParsing = new WineProfile();
        return resForParsing.buildFromJson(jsonObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
