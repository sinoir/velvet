package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

public abstract class Capture extends BaseListingElement {

    @SerializedName("private")
    private boolean private_;

    private AccountMinimal capturer_participant;

    public boolean getPrivate() {
        return private_;
    }

    public void setPrivate(boolean private_) {
        this.private_ = private_;
    }

    public AccountMinimal getCapturerParticipant() {
        return capturer_participant;
    }

    public void setCapturerParticipant(AccountMinimal capturer_participant) {
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
