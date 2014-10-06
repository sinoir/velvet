package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

public abstract class Capture extends BaseListingElement {

    @SerializedName("private")
    private boolean private_;

    public boolean getPrivate() {
        return private_;
    }

    public void setPrivate(boolean private_) {
        this.private_ = private_;
    }

    @Override
    public String toString() {
        return "Capture{" +
                "id='" + getId() + '\'' +
                ", created_at=" + getCreatedAt() +
                ", private_=" + private_ +
                ", context='" + getContext() + '\'' +
                ", e_tag='" + getETag() + '\'' +
                '}';
    }
}
