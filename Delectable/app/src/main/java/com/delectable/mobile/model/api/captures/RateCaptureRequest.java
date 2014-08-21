package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.model.api.BaseRequest;

public class RateCaptureRequest extends BaseRequest {

    private RateCapturePayload payload;

    public RateCaptureRequest(String captureId, int userRating) {
        payload = new RateCapturePayload();
        payload.id = captureId;
        payload.rating = String.valueOf(userRating);
    }

    public static class RateCapturePayload {

        private String id;

        private String rating;
    }
}