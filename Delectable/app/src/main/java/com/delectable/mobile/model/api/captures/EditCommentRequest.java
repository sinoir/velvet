package com.delectable.mobile.model.api.captures;


import com.delectable.mobile.model.api.BaseRequest;

public class EditCommentRequest extends BaseRequest {

    EditCommentPayload payload;

    public EditCommentRequest(String captureId, String commentId, String comment) {
        payload = new EditCommentPayload();
        payload.id = captureId;
        payload.comment_id = commentId;
        payload.comment = comment;
    }

    public static class EditCommentPayload {

        // Capture ID
        String id;

        // Comment ID
        String comment_id;

        String comment;
    }
}
