package com.delectable.mobile.api.models;

import org.json.JSONObject;

import java.util.ArrayList;


public class BaseWine extends Resource {

    String id;

    RatingsSummaryHash ratings_summary;

    String producer_name;

    String name;

    String region_id;

    ArrayList<RegionPath> region_path;

    ArrayList<VarietalsHash> varietal_composition;

    // TODO: Build Wine Profile Object
//    ArrayList<WineProfile> wine_profiles;

    String default_wine_profile_id;

    PhotoHash photo;

    String context;

    String e_tag;

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

    public void setRatingsSummary(RatingsSummaryHash ratings_summary) {
        this.ratings_summary = ratings_summary;
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

    public String getRegionId() {
        return region_id;
    }

    public void setRegionId(String region_id) {
        this.region_id = region_id;
    }

    public ArrayList<RegionPath> getRegionPath() {
        return region_path;
    }

    public void setRegionPath(ArrayList<RegionPath> region_path) {
        this.region_path = region_path;
    }

    public ArrayList<VarietalsHash> getVarietalComposition() {
        return varietal_composition;
    }

    public void setVarietalComposition(ArrayList<VarietalsHash> varietal_composition) {
        this.varietal_composition = varietal_composition;
    }

    //TODO: Build WineProfile object
//    public ArrayList<WineProfile> getWineProfiles() {
//        return wine_profiles;
//    }
//
//    public void setWineProfiles(ArrayList<WineProfile> wine_profiles) {
//        this.wine_profiles = wine_profiles;
//    }

    public String getDefaultWineProfileId() {
        return default_wine_profile_id;
    }

    public void setDefaultWineProfileId(String default_wine_profile_id) {
        this.default_wine_profile_id = default_wine_profile_id;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    class RegionPath {

        String id;

        String name;
    }
}
