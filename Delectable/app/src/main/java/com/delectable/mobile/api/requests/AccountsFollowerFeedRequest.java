package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetailsListing;

import org.json.JSONObject;

import java.util.ArrayList;

public class AccountsFollowerFeedRequest extends BaseRequest {

    public static final String CONTEXT_DETAILS = "details";

    public static final String CONTEXT_MINIMAL = "minimal";

    String before;

    String after;

    /**
     * Make request to get Follower Capture Feed
     *
     * @param contextType - Must be either Details or Minimal
     */
    public AccountsFollowerFeedRequest(String contextType) {
        context = contextType;
    }

    @Override
    public String[] getPayloadFields() {
        // Build Payload Fields according to what is set and not set
        ArrayList<String> fieldsList = new ArrayList<String>();
        if (before != null) {
            fieldsList.add("before");
        }
        if (after != null) {
            fieldsList.add("after");
        }
        String[] fieldsArray = fieldsList.toArray(new String[fieldsList.size()]);
        return fieldsArray;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/follower_feed";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        CaptureDetailsListing resForParsing = new CaptureDetailsListing();
        return resForParsing.buildFromJson(jsonObject);
    }
}
