package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AccountsUpdateSettingRequest extends BaseRequest {

    public static final String TAG = AccountsUpdateSettingRequest.class.getSimpleName();

    //possible keys that we can use in the key parameter

    /**
     * Wine identification
     */
    public static final String PN_CAPTURE_TRANSCRIBED = "pn_capture_transcribed";

    /**
     * Comment on your win
     */
    public static final String PN_COMMENT_ON_OWN_WINE = "pn_comment_on_own_wine";

    /**
     * Respond to comment
     */
    public static final String PN_COMMENT_RESPONSE = "pn_comment_response";

    //no ui equivalent in ios v4.0 app
    public static final String PN_EXPERIMENT = "pn_experiment";

    /**
     * Friend joined delectable
     */
    public static final String PN_FRIEND_JOINED = "pn_friend_joined";

    /**
     * Like your wine
     */
    public static final String PN_LIKE_ON_OWN_WINE = "pn_like_on_own_wine";

    /**
     * Following you
     */
    public static final String PN_NEW_FOLLOWER = "pn_new_follower";

    //no ui equivalent in ios v4.0 app
    public static final String PN_PURCHASE_OFFER_MADE = "pn_purchase_offer_made";

    /**
     * Tagged on a wine
     */
    public static final String PN_TAGGED = "pn_tagged";

    private String key;

    private boolean setting;

    public AccountsUpdateSettingRequest(String key, boolean setting) {
        this.key = key;
        this.setting = setting;
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "key"
        };
    }

    @Override
    public JSONObject buildPayload() {
        JSONObject jsonObject = super.buildPayload();

        //manually adding the boolean because buildPayload() in the superclass only handles string values
        try {
            jsonObject.put("setting", setting);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.wtf(TAG, "Failed to build payload", e);
        }
        return jsonObject;
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/update_setting";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        return null;
        //TODO api response doesn't conform to BaseResponse, unable to call this method successfully
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean getSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {
        this.setting = setting;
    }

    @Override
    public String toString() {
        return AccountsUpdateSettingRequest.class.getSimpleName() + "{" +
                "\"key\": \"" + key + "\"," +
                "\"setting\": \"" + setting + "\"" +
                "} " + super.toString();
    }
}
