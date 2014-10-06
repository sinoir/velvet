package com.delectable.mobile.api.models;

import com.delectable.mobile.model.api.BaseResponse;

import android.os.Parcel;
import android.os.Parcelable;


public class WineProfileMinimal extends BaseResponse implements Parcelable {

    public static final Creator<WineProfileMinimal> CREATOR
            = new Creator<WineProfileMinimal>() {
        public WineProfileMinimal createFromParcel(Parcel source) {
            return new WineProfileMinimal(source);
        }

        public WineProfileMinimal[] newArray(int size) {
            return new WineProfileMinimal[size];
        }
    };

    private String id;

    private String region_id;

    private String vintage;

    private String producer_name;

    private String name;

    private String base_wine_id;

    private double price;

    private String price_status;

    private PhotoHash photo;

    private String description;

    //TODO String/double/int for forwarded id?
    //forwarded id doesn't exist in WineProfileSubprofile
    //private String forwarded_id;

    private String price_text;

    public WineProfileMinimal() {
    }

    protected WineProfileMinimal(Parcel in) {
        super(in);
        this.id = in.readString();
        this.region_id = in.readString();
        this.vintage = in.readString();
        this.producer_name = in.readString();
        this.name = in.readString();
        this.base_wine_id = in.readString();
        this.price_text = in.readString();
        this.price_status = in.readString();
        this.description = in.readString();
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.photo = in.readParcelable(PhotoHash.class.getClassLoader());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionId() {
        return region_id;
    }

    public void setRegionId(String region_id) {
        this.region_id = region_id;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
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

    public String getBaseWineId() {
        return base_wine_id;
    }

    public void setBaseWineId(String base_wine_id) {
        this.base_wine_id = base_wine_id;
    }

    public String getPriceText() {
        return price_text;
    }

    public void setPriceText(String price_text) {
        this.price_text = price_text;
    }

    public String getPriceStatus() {
        return price_status;
    }

    public void setPriceStatus(String price_status) {
        this.price_status = price_status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "WineProfile{" +
                "id='" + id + '\'' +
                ", region_id='" + region_id + '\'' +
                ", vintage='" + vintage + '\'' +
                ", producer_name='" + producer_name + '\'' +
                ", name='" + name + '\'' +
                ", base_wine_id='" + base_wine_id + '\'' +
                ", price_text='" + price_text + '\'' +
                ", price_status='" + price_status + '\'' +
                ", e_tag='" + getETag() + '\'' +
                ", description='" + description + '\'' +
                ", context='" + getContext() + '\'' +
                ", price=" + price +
                ", photo=" + photo +
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
        dest.writeString(this.region_id);
        dest.writeString(this.vintage);
        dest.writeString(this.producer_name);
        dest.writeString(this.name);
        dest.writeString(this.base_wine_id);
        dest.writeString(this.price_text);
        dest.writeString(this.price_status);
        dest.writeString(this.description);
        dest.writeValue(this.price);
        dest.writeParcelable(this.photo, flags);
    }
}
