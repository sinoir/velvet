package com.delectable.mobile.api.models;


import android.os.Parcel;
import android.os.Parcelable;

public class AccountMinimal extends AccountSearch implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(shadowbanned ? (byte) 1 : (byte) 0);
        dest.writeString(this.e_tag);
    }

    public AccountMinimal() {
    }

    protected AccountMinimal(Parcel in) {
        super(in);
        this.shadowbanned = in.readByte() != 0;
        this.e_tag = in.readString();
    }

    public static final Creator<AccountMinimal> CREATOR = new Creator<AccountMinimal>() {
        public AccountMinimal createFromParcel(Parcel source) {
            return new AccountMinimal(source);
        }

        public AccountMinimal[] newArray(int size) {
            return new AccountMinimal[size];
        }
    };
}
