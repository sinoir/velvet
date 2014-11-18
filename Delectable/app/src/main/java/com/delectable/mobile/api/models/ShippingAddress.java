package com.delectable.mobile.api.models;

public class ShippingAddress extends BaseAddress {

    private String id;

    private boolean primary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public String toString() {
        return "ShippingAddress{" +
                "id='" + id + '\'' +
                ", primary=" + primary +
                "} " + super.toString();
    }
}
