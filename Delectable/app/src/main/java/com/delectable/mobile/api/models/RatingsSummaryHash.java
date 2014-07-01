package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RatingsSummaryHash implements Parcelable {

    public static final Parcelable.Creator<RatingsSummaryHash> CREATOR
            = new Parcelable.Creator<RatingsSummaryHash>() {
        public RatingsSummaryHash createFromParcel(Parcel source) {
            return new RatingsSummaryHash(source);
        }

        public RatingsSummaryHash[] newArray(int size) {
            return new RatingsSummaryHash[size];
        }
    };

    Ratings all;

    Ratings pro;

    public RatingsSummaryHash() {
    }


    private RatingsSummaryHash(Parcel in) {
        this.all = new Ratings();
        this.pro = new Ratings();

        this.all.count = in.readInt();
        this.all.avg = in.readDouble();
        this.pro.count = in.readInt();
        this.pro.avg = in.readDouble();
    }

    public int getAllCount() {
        return all != null ? all.count : 0;
    }

    public int getProCount() {
        return pro != null ? pro.count : 0;
    }

    public double getAllAvg() {
        return all != null ? all.avg : 0;
    }

    public double getProAvg() {
        return pro != null ? pro.avg : 0;
    }

    public Ratings getAll() {
        return all;
    }

    public void setAll(Ratings all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return "RatingsSummaryHash{" +
                "all=" + all +
                ", pro=" + pro +
                '}';
    }

    public Ratings getPro() {
        return pro;
    }

    public void setPro(Ratings pro) {
        this.pro = pro;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int allCount = getAllCount();
        dest.writeInt(allCount);
        dest.writeDouble(getAllAvg());
        dest.writeInt(getProCount());
        dest.writeDouble(getProAvg());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RatingsSummaryHash that = (RatingsSummaryHash) o;

        if (all != null ? !all.equals(that.all) : that.all != null) {
            return false;
        }
        if (pro != null ? !pro.equals(that.pro) : that.pro != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = all != null ? all.hashCode() : 0;
        result = 31 * result + (pro != null ? pro.hashCode() : 0);
        return result;
    }

    private class Ratings {

        Integer count;

        Double avg;

        @Override
        public String toString() {
            return "Ratings{" +
                    "count=" + count +
                    ", avg=" + avg +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Ratings ratings = (Ratings) o;

            if (avg != null ? !avg.equals(ratings.avg) : ratings.avg != null) {
                return false;
            }
            if (count != null ? !count.equals(ratings.count) : ratings.count != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = count != null ? count.hashCode() : 0;
            result = 31 * result + (avg != null ? avg.hashCode() : 0);
            return result;
        }
    }
}
