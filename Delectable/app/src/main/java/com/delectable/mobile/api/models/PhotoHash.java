package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

import com.delectable.mobile.model.api.BaseResponse;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class PhotoHash extends BaseResponse implements Parcelable, Serializable {

    public static final Parcelable.Creator<PhotoHash> CREATOR
            = new Parcelable.Creator<PhotoHash>() {
        public PhotoHash createFromParcel(Parcel source) {
            return new PhotoHash(source);
        }

        public PhotoHash[] newArray(int size) {
            return new PhotoHash[size];
        }
    };

    private static final long serialVersionUID = -7494546096166895300L;

    private String url;

    private ChildResolution child_resolutions;

    public PhotoHash() {
    }

    public PhotoHash(String url,
            ChildResolution child_resolutions) {
        this.url = url;
        this.child_resolutions = child_resolutions;
    }

    private PhotoHash(Parcel in) {
        super(in);
        this.url = in.readString();
        this.child_resolutions = in.readParcelable(ChildResolution.class.getClassLoader());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ChildResolution getChildResolutions() {
        return child_resolutions;
    }

    public void setChildResolutions(ChildResolution child_resolutions) {
        this.child_resolutions = child_resolutions;
    }

    public String getNanoUrl() {
        return this.child_resolutions.size_nano;
    }

    public String getMicroUrl() {
        return this.child_resolutions.size_micro;
    }

    public String getThumbUrl() {
        return this.child_resolutions.size_thumb;
    }

    public String get250Url() {
        return this.child_resolutions.size_250;
    }

    public String get450Url() {
        return this.child_resolutions.size_450;
    }

    public String getMediumUrl() {
        return this.child_resolutions.size_medium;
    }

    public String getBlurUrl() {
        return this.child_resolutions.size_blur;
    }

    public String getSmallest() {
        String imageUrl = this.child_resolutions.getSmallest();
        if (imageUrl != null) {
            return imageUrl;
        }
        return this.url;
    }

    public String getLargest() {
        return this.child_resolutions.getLargest();
    }

    public String getBestThumb() {
        String imageUrl = this.child_resolutions != null ? this.child_resolutions.getBestThumb()
                : null;
        if (this.child_resolutions != null && imageUrl != null) {
            return imageUrl;
        }
        return this.url;
    }

    /**
     * see {@link ChildResolution#get450Plus()}
     */
    public String get450Plus() {
        String imageUrl = this.child_resolutions.get450Plus();
        if (imageUrl != null) {
            return imageUrl;
        }
        return this.url;
    }

    @Override
    public String toString() {
        return "PhotoHash{" +
                "url='" + url + '\'' +
                ", child_resolutions=" + child_resolutions +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.url);
        dest.writeParcelable(this.child_resolutions, 0);
    }

    public static class ChildResolution implements Serializable, Parcelable {

        public static final Creator<ChildResolution> CREATOR = new Creator<ChildResolution>() {
            public ChildResolution createFromParcel(Parcel source) {
                return new ChildResolution(source);
            }

            public ChildResolution[] newArray(int size) {
                return new ChildResolution[size];
            }
        };

        private static final long serialVersionUID = 6186001967269219062L;

        @SerializedName("50")
        private String size_nano;

        @SerializedName("110")
        private String size_micro;

        @SerializedName("150")
        private String size_thumb;

        @SerializedName("250")
        private String size_250;

        @SerializedName("450")
        private String size_450;

        @SerializedName("640")
        private String size_medium;

        @SerializedName("1280")
        private String size_blur;

        public ChildResolution() {
        }

        public ChildResolution(String size_nano, String size_micro, String size_thumb,
                String size_250, String size_450, String size_medium, String size_blur) {
            this.size_nano = size_nano;
            this.size_micro = size_micro;
            this.size_thumb = size_thumb;
            this.size_250 = size_250;
            this.size_450 = size_450;
            this.size_medium = size_medium;
            this.size_blur = size_blur;
        }

        private ChildResolution(Parcel in) {
            this.size_nano = in.readString();
            this.size_micro = in.readString();
            this.size_thumb = in.readString();
            this.size_250 = in.readString();
            this.size_450 = in.readString();
            this.size_medium = in.readString();
            this.size_blur = in.readString();
        }

        public String getSmallest() {
            String[] sizes = {
                    size_nano,
                    size_micro,
                    size_thumb,
                    size_250,
                    size_450,
                    size_medium,
                    size_blur
            };
            return getTopPhoto(sizes);
        }

        public String getLargest() {
            String[] sizes = {
                    size_blur,
                    size_medium,
                    size_450,
                    size_250,
                    size_thumb,
                    size_micro,
                    size_nano
            };
            return getTopPhoto(sizes);
        }

        /**
         * Returns a thumbnail size photo if it exists, if not then then it returns the next higher
         * resolution photo.
         */
        public String getBestThumb() {
            String[] sizes = {
                    size_thumb,
                    size_250,
                    size_450,
                    size_medium,
                    size_blur
            };
            return getTopPhoto(sizes);
        }

        /**
         * Returns the 450px or better image url.
         */
        public String get450Plus() {
            String[] sizes = {
                    size_450,
                    size_medium,
                    size_blur
            };
            return getTopPhoto(sizes);
        }

        /**
         * Returns the first non null photo url.
         */
        private String getTopPhoto(String[] photos) {
            for (String size : photos) {
                if (size != null && !size.trim().equals("")) {
                    return size;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "ChildResolution{" +
                    "size_nano='" + size_nano + '\'' +
                    ", size_micro='" + size_micro + '\'' +
                    ", size_thumb='" + size_thumb + '\'' +
                    ", size_250='" + size_250 + '\'' +
                    ", size_450='" + size_450 + '\'' +
                    ", size_medium='" + size_medium + '\'' +
                    ", size_blur='" + size_blur + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.size_nano);
            dest.writeString(this.size_micro);
            dest.writeString(this.size_thumb);
            dest.writeString(this.size_250);
            dest.writeString(this.size_450);
            dest.writeString(this.size_medium);
            dest.writeString(this.size_blur);
        }
    }
}