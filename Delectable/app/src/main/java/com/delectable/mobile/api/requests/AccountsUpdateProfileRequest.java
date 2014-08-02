package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;

import org.json.JSONObject;

import android.text.TextUtils;

import java.util.ArrayList;

public class AccountsUpdateProfileRequest extends BaseRequest {

    private static final String TAG = AccountsUpdateProfileRequest.class.getSimpleName();

    private String fname;

    private String lname;

    private String url;

    private String bio;

    @Override
    public String[] getPayloadFields() {
        ArrayList<String> payload = new ArrayList<String>();
        if (fname != null) {
            payload.add("fname");
        }
        if (lname != null) {
            payload.add("lname");
        }
        if (url != null) {
            payload.add("url");
        }
        if (bio != null) {
            payload.add("bio");
        }
        return payload.toArray(new String[payload.size()]);
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/update_profile";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        return null;
        //TODO api response doesn't conform to BaseResponse, unable to call this method successfully
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
