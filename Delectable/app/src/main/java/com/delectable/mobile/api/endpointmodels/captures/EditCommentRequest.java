package com.delectable.mobile.api.endpointmodels.captures;


import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class EditCommentRequest extends BaseRequest {

    private EditCommentPayload payload;

    public EditCommentRequest(String captureId, String commentId, String comment) {
        payload = new EditCommentPayload();
        payload.id = captureId;
        payload.comment_id = commentId;
        payload.comment = comment;
    }

    public static class EditCommentPayload {

        // Capture ID
        private String id;

        private String comment_id;

        private String comment;
    }
}
