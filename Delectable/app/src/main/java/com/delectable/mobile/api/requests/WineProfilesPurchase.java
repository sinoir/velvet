package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.WineProfile;

import org.json.JSONObject;

// TODO: Test Functionality
public class WineProfilesPurchase extends BaseRequest {

    String id;

    String purchase_offer_id;

    String payment_method_id;

    String shipping_address_id;

    String quantity;

    String additional_comments;


    @Override
    public String[] getPayloadFields() {
        return new String[]{
                "id",
                "purchase_offer_id",
                "payment_method_id",
                "shipping_address_id",
                "quantity",
                "additional_comments",
        };
    }

    @Override
    public String getResourceUrl() {
        return API_VER + "/wine_profiles/purchase";
    }

    @Override
    public WineProfile buildResopnseFromJson(JSONObject jsonObject) {
        return WineProfile.buildFromJson(jsonObject);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseOfferId() {
        return purchase_offer_id;
    }

    public void setPurchaseOfferId(String purchase_offer_id) {
        this.purchase_offer_id = purchase_offer_id;
    }

    public String getPaymentMethodId() {
        return payment_method_id;
    }

    public void setPaymentMethodId(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getShippingAddressId() {
        return shipping_address_id;
    }

    public void setShippingAddressId(String shipping_address_id) {
        this.shipping_address_id = shipping_address_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAdditionalComments() {
        return additional_comments;
    }

    public void setAdditionalComments(String additional_comments) {
        this.additional_comments = additional_comments;
    }

    @Override
    public String toString() {
        return "WineProfilesPurchase{" +
                "id='" + id + '\'' +
                ", purchase_offer_id='" + purchase_offer_id + '\'' +
                ", payment_method_id='" + payment_method_id + '\'' +
                ", shipping_address_id='" + shipping_address_id + '\'' +
                ", quantity='" + quantity + '\'' +
                ", additional_comments='" + additional_comments + '\'' +
                '}';
    }
}
