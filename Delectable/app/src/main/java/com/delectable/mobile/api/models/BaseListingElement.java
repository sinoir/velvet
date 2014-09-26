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

    private String id;

    private double created_at;

    public Date getCreatedAtDate() {
        return DateHelperUtil.dateFromDouble(created_at);
    }

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

    @Override
    public String toString() {
        return "BaseListingElement{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", context='" + getContext() + '\'' +
                ", e_tag='" + getETag() + '\'' +
                '}';
    }
}
