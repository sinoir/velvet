package com.delectable.mobile.api.models;


import org.apache.commons.lang3.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Account that is context search.
 */
public class AccountSearch implements Parcelable {

    public static transient int RELATION_TYPE_SELF = -1;

    public static transient int RELATION_TYPE_NONE = 0;

    public static transient int RELATION_TYPE_FOLLOWING = 1;

    public enum Context {
        SEARCH("search"),
        MINIMAL("minimal"),
        PROFILE("profile"),
        PRIVATE("private");

        private String mContext;

        private Context(String context) {

            mContext = context;
        }

        public String toString() {
            return mContext;
        }
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeString(this.bio);
        dest.writeParcelable(this.photo, 0);
        dest.writeByte(influencer ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.influencer_titles);
        dest.writeString(this.context);
        dest.writeInt(this.current_user_relationship);
    }

    public AccountSearch() {
    }

    private AccountSearch(Parcel in) {
        this.id = in.readString();
        this.fname = in.readString();
        this.lname = in.readString();
        this.bio = in.readString();
        this.photo = in.readParcelable(PhotoHash.class.getClassLoader());
        this.influencer = in.readByte() != 0;
        this.influencer_titles = new ArrayList<String>();
        in.readStringList(this.influencer_titles);
        this.context = in.readString();
        this.current_user_relationship = in.readInt();
    }

    public static final Parcelable.Creator<AccountSearch> CREATOR
            = new Parcelable.Creator<AccountSearch>() {
        public AccountSearch createFromParcel(Parcel source) {
            return new AccountSearch(source);
        }

        public AccountSearch[] newArray(int size) {
            return new AccountSearch[size];
        }
    };
}
