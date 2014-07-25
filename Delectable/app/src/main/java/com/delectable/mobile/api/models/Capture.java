package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

public abstract class Capture extends BaseListingElement {

    @SerializedName("private")
    Boolean private_;

    Account capturer_participant;

    public Boolean getPrivate() {
        return private_;
    }

    public void setPrivate(Boolean private_) {
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
                "private_=" + private_ +
                ", capturer_participant=" + capturer_participant +
                '}';
    }
}
