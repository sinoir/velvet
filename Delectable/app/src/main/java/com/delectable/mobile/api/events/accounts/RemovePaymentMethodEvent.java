package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class RemovePaymentMethodEvent extends BaseEvent {

    private String mPaymentMethodId;

    public RemovePaymentMethodEvent(String paymentMethodId) {
        super(true);
        mPaymentMethodId = paymentMethodId;
    }

    public RemovePaymentMethodEvent(String errorMessage, ErrorUtil errorCode,
            String paymentMethodId) {
        super(errorMessage, errorCode);
        mPaymentMethodId = paymentMethodId;
    }
}
