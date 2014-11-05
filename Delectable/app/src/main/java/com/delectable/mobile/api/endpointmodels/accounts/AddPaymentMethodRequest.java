package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.models.PaymentMethod;

public class AddPaymentMethodRequest extends BaseRequest {

    private Payload payload;

    public AddPaymentMethodRequest(PaymentMethod paymentMethod, boolean isPrimary) {
        this.payload = new Payload(paymentMethod, isPrimary);
    }

    public static class Payload {

        private PaymentMethod payment_method;

        private boolean primary;

        public Payload(PaymentMethod paymentMethod, boolean primary) {
            this.payment_method = paymentMethod;
            this.primary = primary;
        }
    }
}
