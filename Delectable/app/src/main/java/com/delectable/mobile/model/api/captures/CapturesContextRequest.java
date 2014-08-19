package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.model.api.BaseRequest;

public class CapturesContextRequest extends BaseRequest {

    CapturesContextPayload payload;

    public CapturesContextRequest(String captureId) {
        super("details");
        payload = new CapturesContextPayload();
        payload.id = captureId;
    }

    public String getId() {
        return payload.id;
    }

    public void setId(String id) {
        payload.id = id;
    }

    // Tagging ID not used?
    public String getTaggingId() {
        return payload.tagging_id;
    }

    public void setTaggingId(String tagging_id) {
        payload.tagging_id = tagging_id;
    }

    public static class CapturesContextPayload {

        String id;

        String tagging_id;
    }
}
