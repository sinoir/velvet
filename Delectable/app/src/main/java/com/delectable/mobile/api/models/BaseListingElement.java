package com.delectable.mobile.api.models;

import com.delectable.mobile.util.DateHelperUtil;

import java.util.Comparator;
import java.util.Date;

public abstract class BaseListingElement extends BaseResponse {

    /**
     * Sorts Elements by newest to oldest
     */
    public static Comparator<BaseListingElement> CreatedAtDescendingComparator
            = new Comparator<BaseListingElement>() {

        @Override
        public int compare(BaseListingElement lhs, BaseListingElement rhs) {
            if (!(lhs instanceof BaseListingElement) || !(rhs instanceof BaseListingElement)) {
                throw new ClassCastException();
            }
            return (int) (rhs.getCreatedAt() - lhs.getCreatedAt());
        }
    };

    String id;

    Double created_at;

    public Date getCreatedAtDate() {
        double createdAtTime = created_at != null ? created_at.doubleValue() : 0.0f;
        return DateHelperUtil.dateFromDouble(createdAtTime);
    }

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

    @Override
    public String toString() {
        return "BaseListingElement{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
