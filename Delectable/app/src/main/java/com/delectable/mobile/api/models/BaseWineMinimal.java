package com.delectable.mobile.api.models;

import com.delectable.mobile.model.api.BaseResponse;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseWineMinimal extends BaseResponse implements Parcelable {

    private String id;

    private String name;

    private String producer_name;

    private PhotoHash photo;

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

    @Override
    public String toString() {
        return "BaseWineMinimal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", producer_name='" + producer_name + '\'' +
                ", photo=" + photo +
                ", context=" + getContext() +
                ", e_tag=" + getETag() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.producer_name);
        dest.writeParcelable(this.photo, 0);
    }

    protected BaseWineMinimal(Parcel in) {
        super(in);
        this.id = in.readString();
        this.name = in.readString();
        this.producer_name = in.readString();
        this.photo = in.readParcelable(PhotoHash.class.getClassLoader());
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
