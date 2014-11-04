package com.delectable.mobile.api.endpointmodels.wineprofiles;

import com.delectable.mobile.api.endpointmodels.BaseResponse;

public class WineProfilesPurchaseResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private String purchase_order_id;

        public String getPurchaseOrderId() {
            return purchase_order_id;
        }
    }
}
