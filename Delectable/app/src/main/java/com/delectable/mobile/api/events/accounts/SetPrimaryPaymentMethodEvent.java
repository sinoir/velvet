package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class SetPrimaryPaymentMethodEvent extends BaseEvent {

    private String mPaymentMethodId;

    public SetPrimaryPaymentMethodEvent(String paymentMethodId) {
        super(true);
        mPaymentMethodId = paymentMethodId;
    }

    public SetPrimaryPaymentMethodEvent(String errorMessage, ErrorUtil errorCode,
            String paymentMethodId) {
        super(errorMessage, errorCode);
        mPaymentMethodId = paymentMethodId;
    }
}
