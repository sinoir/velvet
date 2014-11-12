package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class RemovedShippingAddressEvent extends BaseEvent {

    public RemovedShippingAddressEvent() {
        super(true);
    }

    public RemovedShippingAddressEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }
}
