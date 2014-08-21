package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.model.api.BaseRequest;

public class RateCaptureRequest extends BaseRequest {

    RateCapturePayload payload;

    public RateCaptureRequest(String captureId, int userRating) {
        payload = new RateCapturePayload();
        payload.id = captureId;
        payload.rating = String.valueOf(userRating);
    }

    public static class RateCapturePayload {

        String id;

        String rating;
    }
}
