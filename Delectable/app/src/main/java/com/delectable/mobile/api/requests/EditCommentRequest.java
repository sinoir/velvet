package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;

import org.json.JSONObject;

public class EditCommentRequest extends BaseRequest {

    private static final String TAG = "CommentCaptureRequest";

    // Capture ID
    String id;

    // Comment ID
    String comment_id;

    String comment;

    // Current Capture, not sent to server.  Used for mergin/updating.
    CaptureDetails mCaptureDetails;

    /**
     * @param captureComment - Capture Comment to update
     */
    public EditCommentRequest(CaptureDetails captureDetails, CaptureComment captureComment) {
        id = captureDetails.getId();
        comment_id = captureComment.getId();
        comment = captureComment.getComment();
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
                "comment_id",
                "comment",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/edit_comment";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        return null;
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

    public String getCommentId() {
        return comment_id;
    }

    public void setCommentId(String comment_id) {
        this.comment_id = comment_id;
    }
}
