package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;

import org.json.JSONObject;

public class CommentCaptureRequest extends BaseRequest {

    private static final String TAG = "CommentCaptureRequest";

    String id;

    String comment;

    // Current Capture, not sent to server.  Used for mergin/updating.
    CaptureDetails mCaptureDetails;

    /**
     * @param capture     - The capture the user wants to comment
     * @param userComment - User comment
     */
    public CommentCaptureRequest(CaptureDetails capture, String userComment) {
        id = capture.getId();
        comment = userComment;
        mCaptureDetails = capture;
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
                "comment",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/comment";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        CaptureDetails updatedCapture = mCaptureDetails.buildFromJson(jsonObject);
        return updatedCapture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
