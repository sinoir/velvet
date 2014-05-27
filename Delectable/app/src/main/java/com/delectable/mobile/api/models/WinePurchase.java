package com.delectable.mobile.api.models;

import com.delectable.mobile.api.Actions;

import org.json.JSONObject;

import android.util.SparseArray;

// TODO: Test out functionality once implementing
public class WinePurchase extends Resource implements Actions.WinePurchaseActions {

    private static final String sBaseUri = API_VER + "/wine_profiles";

    private static final SparseArray<String> sActionUris = new SparseArray<String>();

    private static final SparseArray<String[]> sActionPayloadFields = new SparseArray<String[]>();

    static {
        sActionUris.append(A_PURCHASE, sBaseUri + "/purchase");

        sActionPayloadFields.append(A_PURCHASE, new String[]{
                "id",
                "purchase_offer_id",
                "payment_method_id",
                "shipping_address_id",
                "quantity",
                "additional_comments",
        });
    }

    String id;

    String purchase_offer_id;

    String payment_method_id;

    String shipping_address_id;

    String quantity;

    String additional_comments;

    String purchase_order_id;

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
        WinePurchase newResource = buildFromJson(payloadObj, WinePurchase.class);
        return newResource;
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

    public String getPurchaseOrderId() {
        return purchase_order_id;
    }

    public void setPurchaseOrderId(String purchase_order_id) {
        this.purchase_order_id = purchase_order_id;
    }

    @Override
    public String toString() {
        return "WinePurchase{" +
                "id='" + id + '\'' +
                ", purchase_offer_id='" + purchase_offer_id + '\'' +
                ", payment_method_id='" + payment_method_id + '\'' +
                ", shipping_address_id='" + shipping_address_id + '\'' +
                ", quantity='" + quantity + '\'' +
                ", additional_comments='" + additional_comments + '\'' +
                ", purchase_order_id='" + purchase_order_id + '\'' +
                '}';
    }
}
