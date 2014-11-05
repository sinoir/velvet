package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class ModifyPaymentMethodRequest extends BaseRequest {

    private Payload payload;

    public ModifyPaymentMethodRequest(String payment_method_id) {
        this.payload = new Payload(payment_method_id);
    }

    public static class Payload {

        private String payment_method_id;

        public Payload(String payment_method_id) {
            this.payment_method_id = payment_method_id;
        }
    }
}
