package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.model.api.BaseRequest;

public class CommentCaptureRequest extends BaseRequest {

    private CommentCapturePayload payload;

    public CommentCaptureRequest(String captureId, String userComment) {
        payload = new CommentCapturePayload();
        payload.id = captureId;
        payload.comment = userComment;
    }

    public static class CommentCapturePayload {

        private String id;

        private String comment;
    }
}
