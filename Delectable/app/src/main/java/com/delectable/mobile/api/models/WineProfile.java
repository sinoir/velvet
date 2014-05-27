package com.delectable.mobile.api.models;

import com.delectable.mobile.api.Actions;

import org.json.JSONObject;

import android.util.SparseArray;

public class WineProfile extends Resource implements Actions.WineProfileActions {

    private static final String sBaseUri = API_VER + "/wine_profiles";

    private static final SparseArray<String> sActionUris = new SparseArray<String>();

    private static final SparseArray<String[]> sActionPayloadFields = new SparseArray<String[]>();

    static {
        sActionUris.append(A_CONTEXT, sBaseUri + "/context");
        sActionUris.append(A_WISHLIST, sBaseUri + "/wishlist");

        sActionPayloadFields.append(A_CONTEXT, new String[]{
                "id",
        });

        sActionPayloadFields.append(A_WISHLIST, new String[]{
                "id",
                "action",
        });
    }

    String id;

    boolean action;

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

    PhotoHash photo;


    @Override
    public String[] getPayloadFieldsForAction(int action) {
        return sActionPayloadFields.get(action);
    }

    @Override
    public String getResourceUrlForAction(int action) {
        return sActionUris.get(action);
    }

    @Override
    public Resource parsePayloadForAction(JSONObject jsonObject, int action) {
        JSONObject payloadObj = jsonObject.optJSONObject("payload");
        WineProfile newResource = null;
        if (payloadObj != null && payloadObj.optJSONObject("wine_profile") != null) {
            newResource = buildFromJson(payloadObj.optJSONObject("wine_profile"),
                    this.getClass());
        }

        return newResource;
    }

    public String getResourceContextForAction(int action) {
        if (action == A_CONTEXT) {
            return "subprofile";
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
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

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "WineProfile{" +
                "id='" + id + '\'' +
                ", action=" + action +
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
                ", photo=" + photo +
                '}';
    }
}
