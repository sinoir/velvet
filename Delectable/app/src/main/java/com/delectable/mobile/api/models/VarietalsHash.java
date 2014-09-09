package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;

public class VarietalsHash implements Parcelable {

    private String id;

    private String name;

    private String color;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "VarietalsHash{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
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
        dest.writeString(this.color);
    }

    private VarietalsHash(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.color = in.readString();
    }

    public static final Parcelable.Creator<VarietalsHash> CREATOR
            = new Parcelable.Creator<VarietalsHash>() {
        public VarietalsHash createFromParcel(Parcel source) {
            return new VarietalsHash(source);
        }

        public VarietalsHash[] newArray(int size) {
            return new VarietalsHash[size];
        }
    };
}
