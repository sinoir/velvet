package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.SearchResult;

import org.json.JSONObject;

public class BaseWinesSearch extends BaseSearch {

    @Override
    public String getResourceUrl() {
        return API_VER + "/base_wines/search";
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        SearchResult resForParsing = new SearchResult();
        return resForParsing.buildFromJson(jsonObject);
    }
}
