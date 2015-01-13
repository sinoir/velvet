package com.delectable.mobile.api.endpointmodels.captures;


import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.models.CaptureCommentAttributes;

import java.util.ArrayList;

/**
 * Edits the given comment on the given capture. The current account must be the account that made
 * the comment, or an UNAUTHORIZED error will be returned.
 */
public class CapturesEditCommentRequest extends BaseRequest {

    private Payload payload;

    public CapturesEditCommentRequest(String captureId, String commentId, String comment,
            ArrayList<CaptureCommentAttributes> attributes) {
        payload = new Payload(captureId, commentId, comment, attributes);
    }

    public static class Payload {

        // Capture ID
        private String id;

        private String comment_id;

        private String comment;

        private ArrayList<CaptureCommentAttributes> comment_attributes;

        public Payload(String id, String commentId, String comment,
                ArrayList<CaptureCommentAttributes> attributes) {
            this.id = id;
            this.comment_id = commentId;
            this.comment = comment;
            this.comment_attributes = attributes;
        }
    }
}
