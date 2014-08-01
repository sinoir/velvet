package com.delectable.mobile.api.models;

import org.json.JSONObject;

public class WinePurchase extends BaseResponse {

    String purchase_order_id;

    public static WinePurchase buildFromJson(JSONObject jsonObj) {
        JSONObject payloadObj = jsonObj.optJSONObject("payload");
        WinePurchase newResource = buildFromJson(payloadObj, WinePurchase.class);
        return newResource;
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
                "purchase_order_id='" + purchase_order_id + '\'' +
                '}';
    }
}
