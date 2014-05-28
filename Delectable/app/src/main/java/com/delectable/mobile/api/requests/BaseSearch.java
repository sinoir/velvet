package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.SearchResult;

import org.json.JSONObject;

public abstract class BaseSearch extends BaseRequest {

    String q;

    Integer offset;

    Integer limit;

    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "q",
                "offset",
                "limit"
        };
    }

    @Override
    public BaseResponse buildResopnseFromJson(JSONObject jsonObject) {
        SearchResult resForParsing = new SearchResult();
        return resForParsing.buildFromJson(jsonObject);
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
