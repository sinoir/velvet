package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;

import org.json.JSONObject;

public class RateCaptureRequest extends BaseRequest {

    private static final String TAG = "RateCaptureRequest";

    String id;

    String rating;

    /**
     * @param capture    - The capture the user wants to rate
     * @param userRating - Rating user is giving the capture (0 - 40)
     */
    public RateCaptureRequest(CaptureDetails capture, int userRating) {
        id = capture.getId();
        rating = String.valueOf(userRating);
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
                "rating",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/rate";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        // No Response Payload
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
