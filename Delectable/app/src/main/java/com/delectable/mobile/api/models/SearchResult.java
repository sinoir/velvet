package com.delectable.mobile.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResult extends BaseResponse {

    @Expose
    String q;

    @Expose
    Integer offset;

    @Expose
    Integer limit;

    @Expose
    Integer total;

    @Expose
    Integer search_time;

    ArrayList<SearchHit> hits;

    public static SearchResult buildFromJson(JSONObject jsonObj) {
        JSONObject payloadJson = jsonObj.optJSONObject("payload");
        SearchResult searchResult = buildFromJsonForExposedObjects(payloadJson, SearchResult.class);
        JSONArray jsonHitsArray = payloadJson.optJSONArray("hits");
        // Parse SearchHits based on result type (Either BaseWine or Account)
        if (jsonHitsArray != null &&
                payloadJson != null &&
                jsonHitsArray.length() > 0 &&
                jsonHitsArray.optJSONObject(0).optString("type") != null) {
            String type = jsonHitsArray.optJSONObject(0).optString("type");
            if (type.equalsIgnoreCase("base_wine")) {
                Type listType = new TypeToken<List<SearchHit<BaseWine>>>() {
                }.getType();
                ArrayList<SearchHit> wineHits = new Gson()
                        .fromJson(jsonHitsArray.toString(), listType);
                searchResult.setHits(wineHits);
            }// TODO: Set hits for Accounts
        }
        return searchResult;
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSearchTime() {
        return search_time;
    }

    public void setSearchTime(Integer search_time) {
        this.search_time = search_time;
    }

    public ArrayList<SearchHit> getHits() {
        return hits;
    }

    public void setHits(ArrayList<SearchHit> hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "BaseSearch{" +
                "q='" + q + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                ", total=" + total +
                ", search_time=" + search_time +
                ", hits=" + hits +
                '}';
    }
}
