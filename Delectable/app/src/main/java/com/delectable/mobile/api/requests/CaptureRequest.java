package com.delectable.mobile.api.requests;

import com.google.gson.annotations.SerializedName;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.api.models.TaggeeContact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.util.ArrayList;

public class CaptureRequest extends BaseRequest {

    private static final String TAG = "CaptureRequest";

    String label_scan_id;

    String bucket;

    String filename;

    @SerializedName("private")
    Boolean private_;

    Integer rating;

    Boolean from_camera_roll;

    Boolean share_tw;

    // Only required if share_tw it True
    String user_tw;

    Boolean share_fb;

    Integer capture_longitude;

    Integer capture_latitude;

    String user_country_code;

    String foursquare_location_id;

    ArrayList<TaggeeContact> taggees;

    public CaptureRequest(ProvisionCapture provision) {
        setBucket(provision.getBucket());
        setFilename(provision.getFilename());
    }

    @Override
    public String[] getPayloadFields() {
        // Build Payload Fields according to what is set and not set
        ArrayList<String> fieldsList = new ArrayList<String>();

        if (label_scan_id != null) {
            fieldsList.add("label_scan_id");
        }

        if (bucket != null) {
            fieldsList.add("bucket");
        }

        if (filename != null) {
            fieldsList.add("filename");
        }

        if (private_ != null) {
            fieldsList.add("private");
        }

        if (rating != null) {
            fieldsList.add("rating");
        }

        if (from_camera_roll != null) {
            fieldsList.add("from_camera_roll");
        }

        if (share_tw != null) {
            fieldsList.add("share_tw");
        }

        // Only required if share_tw it True
        if (user_tw != null) {
            fieldsList.add("user_tw");
        }

        if (share_fb != null) {
            fieldsList.add("share_fb");
        }

        if (capture_longitude != null) {
            fieldsList.add("capture_longitude");
        }

        if (capture_latitude != null) {
            fieldsList.add("capture_latitude");
        }

        if (user_country_code != null) {
            fieldsList.add("user_country_code");
        }

        if (foursquare_location_id != null) {
            fieldsList.add("foursquare_location_id");
        }

        String[] fieldsArray = fieldsList.toArray(new String[fieldsList.size()]);
        return fieldsArray;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/captures/capture";
    }

    @Override
    public CaptureDetails buildResopnseFromJson(JSONObject jsonObject) {
        // TODO: Build Response for Capture
        return null;
    }

    @Override
    public JSONObject buildPayload() {
        JSONObject payloadObj = super.buildPayload();
        try {
            JSONArray jsonArray = TaggeeContact.buildJsonArray(getTaggees());
            payloadObj.put("taggees", jsonArray);
        } catch (JSONException e) {
            Log.wtf(TAG, "Failed to Add TaggeeContacts", e);
            e.printStackTrace();
        }
        return payloadObj;
    }


    public void addTaggee(TaggeeContact taggeeContact) {
        if (taggees == null) {
            taggees = new ArrayList<TaggeeContact>();
        }
        taggees.add(taggeeContact);
    }

    public String getLabelScanId() {
        return label_scan_id;
    }

    public void setLabelScanId(String label_scan_id) {
        this.label_scan_id = label_scan_id;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getPrivate() {
        return private_;
    }

    public void setPrivate(Boolean private_) {
        this.private_ = private_;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getFromCameraRoll() {
        return from_camera_roll;
    }

    public void setFromCameraRoll(Boolean from_camera_roll) {
        this.from_camera_roll = from_camera_roll;
    }

    public Boolean getShareTw() {
        return share_tw;
    }

    public void setShareTw(Boolean share_tw) {
        this.share_tw = share_tw;
    }

    public String getUserTw() {
        return user_tw;
    }

    public void setUserTw(String user_tw) {
        this.user_tw = user_tw;
    }

    public Boolean getShareFb() {
        return share_fb;
    }

    public void setShareFb(Boolean share_fb) {
        this.share_fb = share_fb;
    }

    public Integer getCaptureLongitude() {
        return capture_longitude;
    }

    public void setCaptureLongitude(Integer capture_longitude) {
        this.capture_longitude = capture_longitude;
    }

    public Integer getCaptureLatitude() {
        return capture_latitude;
    }

    public void setCaptureLatitude(Integer capture_latitude) {
        this.capture_latitude = capture_latitude;
    }

    public String getUserCountryCode() {
        return user_country_code;
    }

    public void setUserCountryCode(String user_country_code) {
        this.user_country_code = user_country_code;
    }

    public String getFoursquareLocationId() {
        return foursquare_location_id;
    }

    public void setFoursquareLocationId(String foursquare_location_id) {
        this.foursquare_location_id = foursquare_location_id;
    }

    public ArrayList<TaggeeContact> getTaggees() {
        return taggees;
    }

    public void setTaggees(ArrayList<TaggeeContact> taggees) {
        this.taggees = taggees;
    }
}
