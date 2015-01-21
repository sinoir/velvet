package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.api.models.CaptureDetails;

import java.util.ArrayList;

/**
 * Adds a comment to this Capture
 */
public class CapturesCommentRequest extends BaseRequest {

    private Payload payload;

    /**
     * Creates a request object for a {@link CaptureDetails} response.
     *
     * @param captureId   The capture to be commented on
     * @param userComment The comment
     * @param attributes  The comment attributes
     */
    public CapturesCommentRequest(String captureId, String userComment,
            ArrayList<CaptureCommentAttributes> attributes) {
        this(CapturesContext.DETAILS, captureId, userComment, attributes);
    }

    /**
     * @param context     The capture context that you want back as the return object
     * @param captureId   The capture to be commented on
     * @param userComment The comment
     * @param attributes  The comment attributes
     */
    public CapturesCommentRequest(CapturesContext context, String captureId, String userComment,
            ArrayList<CaptureCommentAttributes> attributes) {
        super(context.toString());
        payload = new Payload(captureId, userComment, attributes);
    }


    public static class Payload {

        private String id;

        private String comment;

        private ArrayList<CaptureCommentAttributes> comment_attributes;

        public Payload(String id, String comment) {
            this(id, comment, null);
        }

        public Payload(String id, String comment, ArrayList<CaptureCommentAttributes> attributes) {
            this.id = id;
            this.comment = comment;
            this.comment_attributes = attributes;
        }
    }
}
