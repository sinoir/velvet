package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

public class ModifyShippingAddressRequest extends BaseRequest {

    private Payload payload;

    public ModifyShippingAddressRequest(String address_id) {
        this.payload = new Payload(address_id);
    }

    public static class Payload {

        private String address_id;

        public Payload(String address_id) {
            this.address_id = address_id;
        }
    }
}
