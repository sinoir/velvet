package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.models.ShippingAddress;

public class UpdateShippingAddressRequest extends BaseRequest {

    private Payload payload;

    public UpdateShippingAddressRequest(ShippingAddress address, boolean isPrimary) {
        this.payload = new Payload(address, isPrimary);
    }

    public static class Payload {

        private ShippingAddress address;

        private boolean primary;

        public Payload(ShippingAddress address, boolean primary) {
            this.address = address;
            this.primary = primary;
        }
    }
}
