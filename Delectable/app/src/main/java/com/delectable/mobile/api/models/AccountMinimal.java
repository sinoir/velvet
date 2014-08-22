package com.delectable.mobile.api.models;


import java.util.List;

public class AccountMinimal {

    private String id;

    private String fname;

    private String lname;

    private boolean shadowbanned;

    private PhotoHash photo;

    private boolean influencer;

    private List<String> influencer_titles;

    private String context;

    private String e_tag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public boolean isShadowbanned() {
        return shadowbanned;
    }

    public void setShadowbanned(boolean shadowbanned) {
        this.shadowbanned = shadowbanned;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public boolean isInfluencer() {
        return influencer;
    }

    public void setInfluencer(boolean influencer) {
        this.influencer = influencer;
    }

    public List<String> getInfluencerTitles() {
        return influencer_titles;
    }

    public void setInfluencerTitles(List<String> influencer_titles) {
        this.influencer_titles = influencer_titles;
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


}
