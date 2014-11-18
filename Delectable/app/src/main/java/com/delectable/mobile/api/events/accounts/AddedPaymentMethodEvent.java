package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class AddedPaymentMethodEvent extends BaseEvent {

    public AddedPaymentMethodEvent() {
        super(true);
    }

    public AddedPaymentMethodEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }
}
