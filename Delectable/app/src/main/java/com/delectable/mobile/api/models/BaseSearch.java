package com.delectable.mobile.api.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.Actions;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.SparseArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseSearch extends Resource implements Actions.SearchActions {

    private static final SparseArray<String> sActionUris = new SparseArray<String>();

    private static final String[] sPayloadFields = new String[]{
            "q",
            "offset",
            "limit"
    };

    static {
        sActionUris.append(A_BASE_WINE_SEARCH, API_VER + "/base_wines/search");
        sActionUris.append(A_ACCOUNT_SEARCH, API_VER + "/accounts/search");
    }

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

    @Override
    public String[] getPayloadFieldsForAction(int action) {
        return sPayloadFields;
    }

    @Override
    public String getResourceUrlForAction(int action) {
        return sActionUris.get(action);
    }

    @Override
    public Resource parsePayloadForAction(JSONObject payload, int action) {
        JSONObject payloadJson = payload.optJSONObject("payload");
        BaseSearch searchResult = buildFromJsonForExposedObjects(payloadJson, this.getClass());
        JSONArray jsonHitsArray = payloadJson.optJSONArray("hits");
        if (jsonHitsArray != null) {
            if (action == A_BASE_WINE_SEARCH) {
                Type listType = new TypeToken<List<SearchHit<BaseWine>>>() {
                }.getType();
                ArrayList<SearchHit> wineHits = new Gson()
                        .fromJson(jsonHitsArray.toString(), listType);
                searchResult.setHits(wineHits);
            }
            // TODO: Check for Account results.
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
