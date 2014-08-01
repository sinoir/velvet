package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;

import org.json.JSONObject;

import java.util.ArrayList;

public class CapturesContextRequest extends BaseRequest {

    String id;

    String tagging_id;

    public CapturesContextRequest() {
        // Should we ever need the option to use minimal?
        // Minimal apears to mostly be used inside of other contexts
        context = "details";
    }

    @Override
    public String[] getPayloadFields() {
        // Build Payload Fields according to what is set and not set
        ArrayList<String> fieldsList = new ArrayList<String>();
        if (id != null) {
            fieldsList.add("id");
        }
        if (tagging_id != null) {
            fieldsList.add("tagging_id");
        }
        String[] fieldsArray = fieldsList.toArray(new String[fieldsList.size()]);
        return fieldsArray;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/context";
    }

    @Override
    public CaptureDetails buildResopnseFromJson(JSONObject jsonObject) {
        return CaptureDetails.buildFromJson(jsonObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaggingId() {
        return tagging_id;
    }

    public void setTaggingId(String tagging_id) {
        this.tagging_id = tagging_id;
    }

    @Override
    public String toString() {
        return "CaptureNotesRequest{" +
                "id='" + id + '\'' +
                ", tagging_id='" + tagging_id + '\'' +
                "} " + super.toString();
    }
}
