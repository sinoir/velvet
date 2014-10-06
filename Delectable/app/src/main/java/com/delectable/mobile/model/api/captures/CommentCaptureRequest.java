package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.model.api.BaseRequest;

public class CommentCaptureRequest extends BaseRequest {

    public enum Context {
        MINIMAL("minimal"),
        DETAILS("details");

        private String mLabel;

        private Context(String label) {

            mLabel = label;
        }

        @Override
        public String toString() {
            return mLabel;
        }
    }

    private Payload payload;


    /**
     * Creates a request object for a {@link CaptureDetails} response.
     * @param captureId
     * @param userComment
     */
    public CommentCaptureRequest(String captureId, String userComment) {
        this(Context.DETAILS, captureId, userComment);
    }

    public CommentCaptureRequest(Context context, String captureId, String userComment) {
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
