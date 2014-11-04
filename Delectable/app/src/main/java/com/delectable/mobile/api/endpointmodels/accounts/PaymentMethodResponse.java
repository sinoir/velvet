package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.PaymentMethod;

import java.util.List;

public class PaymentMethodResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private List<PaymentMethod> payment_methods;

        public List<PaymentMethod> getPaymentMethods() {
            return payment_methods;
        }
    }
}
