package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;

import org.json.JSONObject;

public class AccountsSaerch extends BaseSearch {

    @Override
    public String getResourceUrl() {
        return API_VER + "/accounts/search";
    }

}
