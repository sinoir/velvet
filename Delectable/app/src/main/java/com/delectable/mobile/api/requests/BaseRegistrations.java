package com.delectable.mobile.api.requests;

import com.delectable.mobile.Config;
import com.delectable.mobile.api.models.Registration;

import org.json.JSONObject;

public abstract class BaseRegistrations extends BaseRequest {

    protected static final String sBaseUri = API_VER + "/registrations";

    String session_type;

    public BaseRegistrations() {
        // Default SessionType is Mobile
        this.session_type = Config.DEFAULT_SESSION_TYPE;
    }

    public String getSessionType() {
        return session_type;
    }

    public void setSessionType(String session_type) {
        this.session_type = session_type;
    }

    @Override
    public Registration buildResopnseFromJson(JSONObject jsonObject) {
        return Registration.buildFromJson(jsonObject);
    }
}
