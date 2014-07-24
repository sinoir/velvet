package com.delectable.mobile.api.requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.util.ArrayList;

public class FollowAccountsActionRequest extends BaseActionRequest {

    private static final String TAG = "FollowAccountsActionRequest";

    ArrayList<String> ids;

    /**
     * @param accountId    - Account ID to Follow / Unfollow
     * @param shouldFollow - Whether or not to follow the list of users
     */
    public FollowAccountsActionRequest(String accountId, boolean shouldFollow) {
        ids = new ArrayList<String>();
        ids.add(accountId);
        action = shouldFollow;
    }

    /**
     * @param accountIds   - List of Account IDs to Follow / Unfollow
     * @param shouldFollow - Whether or not to follow the list of users
     */
    public FollowAccountsActionRequest(ArrayList<String> accountIds, boolean shouldFollow) {
        ids = accountIds;
        action = shouldFollow;
    }

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "action",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/follow";
    }

    @Override
    public JSONObject buildPayload() {
        JSONObject payloadObj = super.buildPayload();
        try {
            JSONArray jsonArray = new JSONArray();
            if (ids != null) {
                for (String id : ids) {
                    jsonArray.put(id);
                }
                payloadObj.put("ids", jsonArray);
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "Failed to Add TaggeeContacts", e);
            e.printStackTrace();
        }
        return payloadObj;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
}
