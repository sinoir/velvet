package com.delectable.mobile.model.api;

import com.delectable.mobile.model.api.BaseRequest;

public class ActionRequest extends BaseRequest {

    ActionPayload payload;

    public ActionRequest(String id, boolean action) {
        payload = new ActionPayload();
        payload.id = id;
        payload.action = action;
    }

    public static class ActionPayload {

        String id;

        boolean action;
    }
}
