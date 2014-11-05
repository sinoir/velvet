package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class FetchedShippingAddressesEvent extends BaseEvent {

    public FetchedShippingAddressesEvent() {
        super(true);
    }

    public FetchedShippingAddressesEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }
}
