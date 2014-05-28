package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.Registration;

import org.json.JSONObject;

public abstract class BaseRegistrations extends BaseRequest {

    protected static final String sBaseUri = API_VER + "/registrations";

    String session_type;

    protected BaseRegistrations() {
        // Default SessionType is Mobile
        this.session_type = "mobile";
    }

    public String getSessionType() {
        return session_type;
    }

    public void setSessionType(String session_type) {
        this.session_type = session_type;
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        Registration resForParsing = new Registration();
        return resForParsing.buildFromJson(jsonObject);
    }
}
