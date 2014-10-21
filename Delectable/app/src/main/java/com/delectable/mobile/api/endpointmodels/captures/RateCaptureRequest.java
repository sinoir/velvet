package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

/**
 * Rates this Capture as the current user. Must be the capturer or a taggee.
 */
public class RateCaptureRequest extends BaseRequest {

    private Payload payload;

    public RateCaptureRequest(String captureId, int userRating) {
        payload = new Payload(captureId, String.valueOf(userRating));
    }

    public static class Payload {

        private String id;

        private String rating;

        public Payload(String id, String rating) {
            this.id = id;
            this.rating = rating;
        }
    }
}
