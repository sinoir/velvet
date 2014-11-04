package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.models.CaptureDetails;

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
     */
    public CapturesCommentRequest(String captureId, String userComment) {
        this(CapturesContext.DETAILS, captureId, userComment);
    }

    /**
     * @param context     The capture context that you want back as the return object
     * @param captureId   The capture to be commented on
     * @param userComment The comment
     */
    public CapturesCommentRequest(CapturesContext context, String captureId, String userComment) {
        super(context.toString());
        payload = new Payload(captureId, userComment);
    }


    public static class Payload {

        private String id;

        private String comment;

        public Payload(String id, String comment) {
            this.id = id;
            this.comment = comment;
        }
    }
}
