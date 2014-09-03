package com.delectable.mobile.api.models;

import java.util.ArrayList;
import com.delectable.mobile.model.api.BaseResponse;


public class SearchResult<T> extends BaseResponse {

    private String q;

    private int offset;

    private int limit;

    private int total;

    private int search_time;

    ArrayList<SearchHit<T>> hits;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSearchTime() {
        return search_time;
    }

    public void setSearchTime(int search_time) {
        this.search_time = search_time;
    }

    public ArrayList<SearchHit<T>> getHits() {
        return hits;
    }

    public void setHits(ArrayList<SearchHit<T>> hits) {
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
