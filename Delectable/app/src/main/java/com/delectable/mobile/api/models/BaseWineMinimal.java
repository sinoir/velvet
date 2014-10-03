package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseWineMinimal implements Parcelable {

    private String id;

    private String name;

    private String producer_name;

    private PhotoHash photo;

    private String e_tag;

    private String context;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducerName() {
        return producer_name;
    }

    public void setProducerName(String producer_name) {
        this.producer_name = producer_name;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "BaseWineMinimal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", producer_name='" + producer_name + '\'' +
                ", photo=" + photo +
                ", context=" + context +
                ", e_tag=" + e_tag +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.producer_name);
        dest.writeParcelable(this.photo, 0);
        dest.writeString(this.e_tag);
        dest.writeString(this.context);
    }

    public BaseWineMinimal() {
    }

    protected BaseWineMinimal(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.producer_name = in.readString();
        this.photo = in.readParcelable(PhotoHash.class.getClassLoader());
        this.e_tag = in.readString();
        this.context = in.readString();
    }

    public static final Creator<BaseWineMinimal> CREATOR = new Creator<BaseWineMinimal>() {
        public BaseWineMinimal createFromParcel(Parcel source) {
            return new BaseWineMinimal(source);
        }

        public BaseWineMinimal[] newArray(int size) {
            return new BaseWineMinimal[size];
        }
    };
}
