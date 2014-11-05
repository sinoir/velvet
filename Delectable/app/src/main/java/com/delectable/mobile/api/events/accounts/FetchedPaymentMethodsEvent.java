package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class FetchedPaymentMethodsEvent extends BaseEvent {

    public FetchedPaymentMethodsEvent() {
        super(true);
    }

    public FetchedPaymentMethodsEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }
}
