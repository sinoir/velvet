package com.delectable.mobile.api.models;

import com.delectable.mobile.api.Actions;

import org.json.JSONObject;

import android.util.SparseArray;

public class WineSource extends Resource implements Actions.WineSourceActions {

    private static final String sBaseUri = API_VER + "/wine_profiles";

    private static final SparseArray<String> sActionUris = new SparseArray<String>();

    private static final SparseArray<String[]> sActionPayloadFields = new SparseArray<String[]>();

    static {
        sActionUris.append(A_SOURCE, sBaseUri + "/source");

        sActionPayloadFields.append(A_SOURCE, new String[]{
                "id",
                "state",
        });
    }

    String id;

    String state;

    WineProfile wine_profile;

    PurchasedOffer purchase_offer;

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
        WineSource newResource = buildFromJson(payloadObj, WineSource.class);
        return newResource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    /**
     * Set State as in US State / Province.
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    public WineProfile getWineProfile() {
        return wine_profile;
    }

    public void setWineProfile(WineProfile wine_profile) {
        this.wine_profile = wine_profile;
    }

    public PurchasedOffer getPurchaseOffer() {
        return purchase_offer;
    }

    public void setPurchaseOffer(PurchasedOffer purchase_offer) {
        this.purchase_offer = purchase_offer;
    }

    @Override
    public String toString() {
        return "WineSource{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", wine_profile=" + wine_profile +
                ", purchase_offer=" + purchase_offer +
                '}';
    }
}
