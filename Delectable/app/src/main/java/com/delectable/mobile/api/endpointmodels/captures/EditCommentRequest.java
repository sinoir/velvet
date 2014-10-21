package com.delectable.mobile.api.endpointmodels.captures;


import com.delectable.mobile.api.endpointmodels.BaseRequest;

/**
 * Edits the given comment on the given capture. The current account must be the account that made
 * the comment, or an UNAUTHORIZED error will be returned.
 */
public class EditCommentRequest extends BaseRequest {

    private Payload payload;

    public EditCommentRequest(String captureId, String commentId, String comment) {
        payload = new Payload(captureId, commentId, comment);
    }

    public static class Payload {

        // Capture ID
        private String id;

        private String comment_id;

        private String comment;

        public Payload(String id, String comment_id, String comment) {
            this.id = id;
            this.comment_id = comment_id;
            this.comment = comment;
        }
    }
}
