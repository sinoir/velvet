package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.CaptureNote;

public class HelpfulActionRequest extends BaseActionRequest {

    /**
     * @param capture   - The CaptureNote the user found helpful/unhelpful
     * @param isHelpful - True if user found the capture note helpful
     */
    public HelpfulActionRequest(CaptureNote capture, boolean isHelpful) {
        id = capture.getId();
        action = isHelpful;
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/helpful";
    }
}
