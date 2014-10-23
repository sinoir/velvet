package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;


public class  WineProfileSubProfile implements Parcelable, Ratingsable {

    private String id;

    //ratings summary doesn't exist in minimal context
    private RatingsSummaryHash ratings_summary;

    private String vintage;

    private String base_wine_id;

    //price can return null from API
    private Double price;

    private String price_status;

    private PhotoHash photo;

    private String description;

    private String price_text;

    private String e_tag;

    private String context;

    public WineProfileSubProfile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RatingsSummaryHash getRatingsSummary() {
        return ratings_summary;
    }

    public void setRatingsSummary(RatingsSummaryHash ratings) {
        this.ratings_summary = ratings;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
        return "WineProfileSubProfile{" +
                "id='" + id + '\'' +
                ", ratings_summary=" + ratings_summary +
                ", vintage='" + vintage + '\'' +
                ", base_wine_id='" + base_wine_id + '\'' +
                ", price=" + price +
                ", price_status='" + price_status + '\'' +
                ", photo=" + photo +
                ", description='" + description + '\'' +
                ", price_text='" + price_text + '\'' +
                ", e_tag='" + e_tag + '\'' +
                ", context='" + context + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.ratings_summary, 0);
        dest.writeString(this.vintage);
        dest.writeString(this.base_wine_id);
        dest.writeValue(this.price);
        dest.writeString(this.price_status);
        dest.writeParcelable(this.photo, 0);
        dest.writeString(this.description);
        dest.writeString(this.price_text);
        dest.writeString(this.e_tag);
        dest.writeString(this.context);
    }

    protected WineProfileSubProfile(Parcel in) {
        this.id = in.readString();
        this.ratings_summary = in.readParcelable(RatingsSummaryHash.class.getClassLoader());
        this.vintage = in.readString();
        this.base_wine_id = in.readString();
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.price_status = in.readString();
        this.photo = in.readParcelable(PhotoHash.class.getClassLoader());
        this.description = in.readString();
        this.price_text = in.readString();
        this.e_tag = in.readString();
        this.context = in.readString();
    }

    public static final Creator<WineProfileSubProfile> CREATOR
            = new Creator<WineProfileSubProfile>() {
        public WineProfileSubProfile createFromParcel(Parcel source) {
            return new WineProfileSubProfile(source);
        }

        public WineProfileSubProfile[] newArray(int size) {
            return new WineProfileSubProfile[size];
        }
    };
}
