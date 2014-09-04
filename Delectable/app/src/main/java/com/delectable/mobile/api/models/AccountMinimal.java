package com.delectable.mobile.api.models;


import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AccountMinimal extends AccountSearch{

    private boolean shadowbanned;

    private String e_tag;

    public boolean isShadowbanned() {
        return shadowbanned;
    }

    public void setShadowbanned(boolean shadowbanned) {
        this.shadowbanned = shadowbanned;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    @Override
    public String toString() {
        return "AccountMinimal{" +
                "id='" + getId() + '\'' +
                ", fname='" + getFname() + '\'' +
                ", lname='" + getLname() + '\'' +
                ", bio='" + getBio() + '\'' +
                ", photo=" + getPhoto() +
                ", influencer=" + isInfluencer() +
                ", influencer_titles=" + getInfluencerTitles() +
                ", context='" + getContext() + '\'' +
                ", current_user_relationship=" + getCurrentUserRelationship() +
                ", shadowbanned=" + shadowbanned +
                ", e_tag='" + e_tag + '\'' +
                '}';
    }

}
