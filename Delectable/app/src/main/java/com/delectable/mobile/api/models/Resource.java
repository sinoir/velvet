package com.delectable.mobile.api.models;

import org.json.JSONObject;

/**
 * Created by abednarek on 5/21/14.
 */
public abstract class Resource {

    protected static final String API_VER = "/v2";

    public JSONObject buildPayloadForAction(int action) {
        // TODO: Parse data using reflection
        String[] fields = getPayloadFieldsForAction(action);
        // TODO: Build out payload and stuff..
        JSONObject obj = new JSONObject();
        return obj;
    }

    public abstract String[] getPayloadFieldsForAction(int action);

    public abstract String getResourceUrlForAction(int action);

    public abstract String parsePayloadForAction(JSONObject payload, int action);
}
