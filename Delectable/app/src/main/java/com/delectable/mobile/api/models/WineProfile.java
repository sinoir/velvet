package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * WineProfile subprofile Context
 */
public class WineProfile extends WineProfileMinimal implements Parcelable {

    public static final Parcelable.Creator<WineProfile> CREATOR
            = new Parcelable.Creator<WineProfile>() {
        public WineProfile createFromParcel(Parcel source) {
            return new WineProfile(source);
        }

        public WineProfile[] newArray(int size) {
            return new WineProfile[size];
        }
    };

    private RatingsSummaryHash ratings_summary;

    public WineProfile() {
    }

    private WineProfile(Parcel in) {
        super(in);
        this.ratings_summary = in.readParcelable(RatingsSummaryHash.class.getClassLoader());
    }

    public RatingsSummaryHash getRatingsSummary() {
        return ratings_summary;
    }

    public void setRatingsSummary(RatingsSummaryHash ratings) {
        this.ratings_summary = ratings;
    }

    @Override
    public String toString() {
        return "WineProfile{" +
                "id='" + getId() + '\'' +
                ", ratings_summary=" + ratings_summary +
                ", region_id='" + getRegionId() + '\'' +
                ", vintage='" + getVintage() + '\'' +
                ", producer_name='" + getProducerName() + '\'' +
                ", name='" + getName() + '\'' +
                ", base_wine_id='" + getBaseWineId() + '\'' +
                ", price_text='" + getPriceText() + '\'' +
                ", price_status='" + getPriceStatus() + '\'' +
                ", e_tag='" + getETag() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", context='" + getContext() + '\'' +
                ", price=" + getPrice() +
                ", photo=" + getPhoto() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.ratings_summary, 0);
    }
}
