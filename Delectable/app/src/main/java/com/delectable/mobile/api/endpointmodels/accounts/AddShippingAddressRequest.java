package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.models.BaseAddress;

public class AddShippingAddressRequest extends BaseRequest {

    private Payload payload;

    public AddShippingAddressRequest(BaseAddress address, boolean isPrimary) {
        this.payload = new Payload(address, isPrimary);
    }

    public static class Payload {

        private BaseAddress address;

        private boolean primary;

        public Payload(BaseAddress address, boolean primary) {
            this.address = address;
            this.primary = primary;
        }
    }
}
