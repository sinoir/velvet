package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

public abstract class Capture extends BaseResponse {

    String id;

    Double created_at;

    @SerializedName("private")
    Boolean private_;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Double created_at) {
        this.created_at = created_at;
    }

    public Boolean getPrivate() {
        return private_;
    }

    public void setPrivate(Boolean private_) {
        this.private_ = private_;
    }

    @Override
    public String toString() {
        return "Capture{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", private_=" + private_ +
                "} " + super.toString();
    }
}
