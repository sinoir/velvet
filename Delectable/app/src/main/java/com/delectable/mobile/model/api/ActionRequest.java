package com.delectable.mobile.model.api;

public class ActionRequest extends BaseRequest {

    ActionPayload payload;

    public ActionRequest(String id) {
        payload = new ActionPayload();
        payload.id = id;
    }

    public ActionRequest(String id, boolean action) {
        payload = new ActionPayload();
        payload.id = id;
        payload.action = action;
    }

    public static class ActionPayload {

        String id;

        Boolean action;
    }
}
