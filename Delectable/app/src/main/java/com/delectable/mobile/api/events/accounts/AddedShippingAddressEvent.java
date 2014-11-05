package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class AddedShippingAddressEvent extends BaseEvent {

    public AddedShippingAddressEvent() {
        super(true);
    }

    public AddedShippingAddressEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }
}
