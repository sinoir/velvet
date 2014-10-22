package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;


public class WineProfileMinimal extends WineProfileSubProfile implements Parcelable {

    private String region_id;

    private String producer_name;

    private String name;

    //TODO String/double/int for forwarded id?
    //forwarded id doesn't exist in WineProfileSubprofile
    //private String forwarded_id;

    public WineProfileMinimal() {
    }

    public String getRegionId() {
        return region_id;
    }

    public void setRegionId(String region_id) {
        this.region_id = region_id;
    }

    public String getProducerName() {
        return producer_name;
    }

    public void setProducerName(String producer_name) {
        this.producer_name = producer_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WineProfileMinimal{" +
                "id='" + getId() + '\'' +
                "region_id='" + region_id + '\'' +
                ", vintage='" + getVintage() + '\'' +
                ", producer_name='" + producer_name + '\'' +
                ", name='" + name + '\'' +
                ", base_wine_id='" + getBaseWineId() + '\'' +
                ", price=" + getPrice() +
                ", price_status='" + getPriceStatus() + '\'' +
                ", photo=" + getPhoto() +
                ", description='" + getDescription() + '\'' +
                ", price_text='" + getPriceText() + '\'' +
                ", e_tag='" + getETag() + '\'' +
                ", context='" + getContext() + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.region_id);
        dest.writeString(this.producer_name);
        dest.writeString(this.name);
    }

    protected WineProfileMinimal(Parcel in) {
        super(in);
        this.region_id = in.readString();
        this.producer_name = in.readString();
        this.name = in.readString();
    }

    public static final Creator<WineProfileMinimal> CREATOR = new Creator<WineProfileMinimal>() {
        public WineProfileMinimal createFromParcel(Parcel source) {
            return new WineProfileMinimal(source);
        }

        public WineProfileMinimal[] newArray(int size) {
            return new WineProfileMinimal[size];
        }
    };
}
