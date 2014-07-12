package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RegionPath implements Parcelable {

    String id;

    String name;

    public RegionPath() {
    }

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

    @Override
    public String toString() {
        return "RegionPath{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
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
    }

    private RegionPath(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<RegionPath> CREATOR
            = new Parcelable.Creator<RegionPath>() {
        public RegionPath createFromParcel(Parcel source) {
            return new RegionPath(source);
        }

        public RegionPath[] newArray(int size) {
            return new RegionPath[size];
        }
    };
}
