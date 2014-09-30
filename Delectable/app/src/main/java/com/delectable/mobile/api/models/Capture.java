package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

public abstract class Capture extends BaseListingElement {

    @SerializedName("private")
    private boolean private_;

    private Account capturer_participant;

    public boolean getPrivate() {
        return private_;
    }

    public void setPrivate(boolean private_) {
        this.private_ = private_;
    }

    public Account getCapturerParticipant() {
        return capturer_participant;
    }

    public void setCapturerParticipant(Account capturer_participant) {
        this.capturer_participant = capturer_participant;
    }

    @Override
    public String toString() {
        return "Capture{" +
                "id='" + getId() + '\'' +
                ", created_at=" + getCreatedAt() +
                ", private_=" + private_ +
                ", context='" + getContext() + '\'' +
                ", e_tag='" + getETag() + '\'' +
                ", capturer_participant=" + capturer_participant +
                '}';
    }
}
