package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class RemovePaymentMethodEvent extends BaseEvent {

    public RemovePaymentMethodEvent() {
        super(true);
    }

    public RemovePaymentMethodEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }
}
