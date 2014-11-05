package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class SetPrimaryShippingAddressEvent extends BaseEvent {

    private String mShippingAddressId;

    public SetPrimaryShippingAddressEvent(String shippingAddressId) {
        super(true);
        mShippingAddressId = shippingAddressId;
    }

    public SetPrimaryShippingAddressEvent(String errorMessage, ErrorUtil errorCode,
            String shippingAddressId) {
        super(errorMessage, errorCode);
        mShippingAddressId = shippingAddressId;
    }

    public String getShippingAddressId() {
        return mShippingAddressId;
    }
}
