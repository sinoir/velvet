package com.delectable.mobile.api.models;

import org.json.JSONObject;

public class WineProfile extends Resource {

    String id;

    RatingsSummaryHash ratings_summary;

    String region_id;

    String vintage;

    String producer_name;

    String name;

    String base_wine_id;

    String price_text;

    String price_status;

    String e_tag;

    String description;

    String context;

    Double price;

    @Override
    public String[] getPayloadFieldsForAction(int action) {
        return new String[0];
    }

    @Override
    public String getResourceUrlForAction(int action) {
        return null;
    }

    @Override
    public Resource parsePayloadForAction(JSONObject payload, int action) {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RatingsSummaryHash getRatingsSummary() {
        return ratings_summary;
    }

    public void setRatingsSummary(RatingsSummaryHash ratings) {
        this.ratings_summary = ratings;
    }

    public String getRegionId() {
        return region_id;
    }

    public void setRegionId(String region_id) {
        this.region_id = region_id;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public String getProducerName() {
        return producer_name;
    }

    public void setProducerName(String producer_name) {
        this.producer_name = producer_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseWineId() {
        return base_wine_id;
    }

    public void setBaseWineId(String base_wine_id) {
        this.base_wine_id = base_wine_id;
    }

    public String getPriceText() {
        return price_text;
    }

    public void setPriceText(String price_text) {
        this.price_text = price_text;
    }

    public String getPriceStatus() {
        return price_status;
    }

    public void setPriceStatus(String price_status) {
        this.price_status = price_status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "WineProfile{" +
                "id='" + id + '\'' +
                ", ratings_summary=" + ratings_summary +
                ", region_id='" + region_id + '\'' +
                ", vintage='" + vintage + '\'' +
                ", producer_name='" + producer_name + '\'' +
                ", name='" + name + '\'' +
                ", base_wine_id='" + base_wine_id + '\'' +
                ", price_text='" + price_text + '\'' +
                ", price_status='" + price_status + '\'' +
                ", e_tag='" + e_tag + '\'' +
                ", description='" + description + '\'' +
                ", context='" + context + '\'' +
                ", price=" + price +
                '}';
    }
}
