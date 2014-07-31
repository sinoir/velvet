package com.delectable.mobile.api.models;

import org.json.JSONObject;

public class WineSource extends BaseResponse {

    WineProfile wine_profile;

    PurchasedOffer purchase_offer;

    public static WineSource buildFromJson(JSONObject jsonObject) {
        JSONObject payloadObj = jsonObject.optJSONObject("payload");
        WineSource newResource = buildFromJson(payloadObj, WineSource.class);
        return newResource;
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
                "wine_profile=" + wine_profile +
                ", purchase_offer=" + purchase_offer +
                '}';
    }
}
