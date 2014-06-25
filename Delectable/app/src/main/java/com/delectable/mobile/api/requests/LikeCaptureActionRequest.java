package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.CaptureDetails;

public class LikeCaptureActionRequest extends BaseActionRequest {

    /**
     * @param capture          - The capture the user wishes to like / unlike
     * @param userLikesCapture - True if user Likes, False if user is unliking
     */
    public LikeCaptureActionRequest(CaptureDetails capture, boolean userLikesCapture) {
        id = capture.getId();
        action = userLikesCapture;
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/like";
    }
}
