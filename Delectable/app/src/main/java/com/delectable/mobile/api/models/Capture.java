package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public abstract class Capture extends BaseResponse {

    /**
     * Sorts Captures by newest to oldest
     */
    public static Comparator<Capture> CreatedAtDescendingComparator = new Comparator<Capture>() {

        @Override
        public int compare(Capture lhs, Capture rhs) {
            if (!(lhs instanceof Capture) || !(rhs instanceof Capture)) {
                throw new ClassCastException();
            }
            return (int) (rhs.getCreatedAt() - lhs.getCreatedAt());
        }
    };

    String id;

    Double created_at;

    @SerializedName("private")
    Boolean private_;

    Account capturer_participant;

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

    public Account getCapturerParticipant() {
        return capturer_participant;
    }

    public void setCapturerParticipant(Account capturer_participant) {
        this.capturer_participant = capturer_participant;
    }

    @Override
    public String toString() {
        return "Capture{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", private_=" + private_ +
                ", capturer_participant=" + capturer_participant +
                "} " + super.toString();
    }
}
