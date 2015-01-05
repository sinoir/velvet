package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class CaptureComment {

    String id;

    double created_at;

    String comment;

    String account_id;

    ArrayList<CaptureCommentAttributes> comment_attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(double created_at) {
        this.created_at = created_at;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAccountId() {
        return account_id;
    }

    public void setAccountId(String account_id) {
        this.account_id = account_id;
    }

    public ArrayList<CaptureCommentAttributes> getCommentAttributes() {
        return comment_attributes;
    }

    public void setCommentAttributes(ArrayList<CaptureCommentAttributes> comment_attributes) {
        this.comment_attributes = comment_attributes;
    }

    @Override
    public String toString() {
        return "CaptureComment{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", comment='" + comment + '\'' +
                ", account_id='" + account_id + '\'' +
                ", comment_attributes=" + comment_attributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaptureComment that = (CaptureComment) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
