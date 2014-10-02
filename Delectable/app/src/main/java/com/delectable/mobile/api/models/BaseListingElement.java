package com.delectable.mobile.api.models;

import com.delectable.mobile.util.DateHelperUtil;

import java.util.Comparator;
import java.util.Date;

public abstract class BaseListingElement {

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

    private String context;

    private String e_tag;

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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    @Override
    public String toString() {
        return "BaseListingElement{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", context='" + context + '\'' +
                ", e_tag='" + e_tag + '\'' +
                '}';
    }
}
