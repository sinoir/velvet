package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.ShippingAddress;

import java.util.List;

public class ShippingAddressesResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private List<ShippingAddress> shipping_addresses;

        public List<ShippingAddress> getShippingAddresses() {
            return shipping_addresses;
        }
    }
}
