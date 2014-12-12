package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.ShippingAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShippingAddressModel {

    private HashMap<String, ShippingAddress> mData = new HashMap<String, ShippingAddress>();

    @Inject
    public ShippingAddressModel() {
    }

    public void saveShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress == null) {
            return;
        }
        mData.put(shippingAddress.getId(), shippingAddress);
    }

    public void saveShippingAddressAsPrimary(ShippingAddress primaryShippingAddress) {
        if (primaryShippingAddress == null) {
            return;
        }
        // Reset old primary shipping address to false
        for (ShippingAddress shippingAddress : mData.values()) {
            shippingAddress.setPrimary(false);
        }
        primaryShippingAddress.setPrimary(true);
        mData.put(primaryShippingAddress.getId(), primaryShippingAddress);
    }

    public void saveShippingAddresses(List<ShippingAddress> shippingAddresses) {
        if (shippingAddresses == null) {
            return;
        }
        for (ShippingAddress shippingAddress : shippingAddresses) {
            saveShippingAddress(shippingAddress);
        }
    }

    public ShippingAddress getShippingAddress(String id) {
        return mData.get(id);
    }

    public ShippingAddress getPrimaryShippingAddress() {
        for (ShippingAddress shippingAddress : mData.values()) {
            if (shippingAddress.getPrimary()) {
                return shippingAddress;
            }
        }
        return null;
    }

    public List<ShippingAddress> getAllShippingAddresses() {
        ArrayList<ShippingAddress> addresses = new ArrayList<ShippingAddress>();
        for (ShippingAddress shippingAddress : mData.values()) {
            addresses.add(shippingAddress);
        }
        return addresses;
    }

    public void removeShippingAddress(String id) {
        if (mData.containsKey(id)) {
            mData.remove(id);
        }
    }

    public void clear() {
        mData.clear();
    }

    /**
     * DO NOT USE DIRECTLY, only used for testing
     */
    public HashMap<String, ShippingAddress> getData() {
        return mData;
    }
}
