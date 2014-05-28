package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureNoteListing;

import org.json.JSONObject;

import java.util.ArrayList;

public class CaptureNotesRequest extends BaseRequest {

    String base_wine_id;

    String wine_profile_id;

    Double before;

    Double after;

    String include_capture_note;

    @Override
    public String[] getPayloadFields() {
        // Build Payload Fields according to what is set and not set
        ArrayList<String> fieldsList = new ArrayList<String>();
        if (base_wine_id != null) {
            fieldsList.add("base_wine_id");
        }
        if (wine_profile_id != null) {
            fieldsList.add("wine_profile_id");
        }
        if (before != null) {
            fieldsList.add("before");
        }
        if (after != null) {
            fieldsList.add("after");
        }
        if (include_capture_note != null) {
            fieldsList.add("include_capture_note");
        }
        String[] fieldsArray = fieldsList.toArray(new String[fieldsList.size()]);
        return fieldsArray;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/notes";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        CaptureNoteListing resForParsing = new CaptureNoteListing();
        return resForParsing.buildFromJson(jsonObject);
    }

    public String getBaseWineId() {
        return base_wine_id;
    }

    public void setBaseWineId(String base_wine_id) {
        this.base_wine_id = base_wine_id;
    }

    public String getWineProfileId() {
        return wine_profile_id;
    }

    public void setWineProfileId(String wine_profile_id) {
        this.wine_profile_id = wine_profile_id;
    }

    public Double getBefore() {
        return before;
    }

    public void setBefore(Double before) {
        this.before = before;
    }

    public Double getAfter() {
        return after;
    }

    public void setAfter(Double after) {
        this.after = after;
    }

    public String getIncludeCaptureNote() {
        return include_capture_note;
    }

    public void setIncludeCaptureNote(String include_capture_note) {
        this.include_capture_note = include_capture_note;
    }

    @Override
    public String toString() {
        return "CaptureNotesRequest{" +
                "base_wine_id='" + base_wine_id + '\'' +
                ", wine_profile_id='" + wine_profile_id + '\'' +
                ", before=" + before +
                ", after=" + after +
                ", include_capture_note='" + include_capture_note + '\'' +
                "} " + super.toString();
    }
}
