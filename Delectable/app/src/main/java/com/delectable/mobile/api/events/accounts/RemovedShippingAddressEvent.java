package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class RemovedShippingAddressEvent extends BaseEvent {

    private String mShippingAddressId;

    public RemovedShippingAddressEvent(String shippingAddressId) {
        super(true);
        mShippingAddressId = shippingAddressId;
    }

    public RemovedShippingAddressEvent(String errorMessage, ErrorUtil errorCode,
            String shippingAddressId) {
        super(errorMessage, errorCode);
        mShippingAddressId = shippingAddressId;
    }

    public String getShippingAddressId() {
        return mShippingAddressId;
    }
}
