package com.delectable.mobile.api.models;


import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Account that is context search.
 */
public class AccountSearch {

    public static transient int RELATION_TYPE_SELF = -1;

    public static transient int RELATION_TYPE_NONE = 0;

    public static transient int RELATION_TYPE_FOLLOWING = 1;

    private String id;

    private String fname;

    private String lname;

    //doesn't exist in AccountMinimal subclass, but exists in AccountProfile and AccountPrivate
    private String bio;

    private PhotoHash photo;

    private boolean influencer;

    private List<String> influencer_titles;

    private String context;

    private int current_user_relationship;


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

    public String getFullName() {
        return getFname() + " " + getLname();
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    /**
     * @return Returns influencer titles in a comma delimited String.
     */
    public String getInfluencerTitlesString() {
        return StringUtils.join(influencer_titles, ",");
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getCurrentUserRelationship() {
        return current_user_relationship;
    }

    public void setCurrentUserRelationship(int current_user_relationship) {
        this.current_user_relationship = current_user_relationship;
    }

    public boolean isUserRelationshipTypeSelf() {
        return checkRelationship(RELATION_TYPE_SELF);
    }

    public boolean isUserRelationshipTypeFollowing() {
        return checkRelationship(RELATION_TYPE_FOLLOWING);
    }

    public boolean isUserRelationshipTypeNone() {
        return checkRelationship(RELATION_TYPE_NONE);
    }

    private boolean checkRelationship(int relationshipType) {
        return current_user_relationship == relationshipType;
    }

    @Override
    public String toString() {
        return "AccountSearch{" +
                "id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", bio='" + bio + '\'' +
                ", photo=" + photo +
                ", influencer=" + influencer +
                ", influencer_titles=" + influencer_titles +
                ", context='" + context + '\'' +
                ", current_user_relationship=" + current_user_relationship +
                '}';
    }
}
