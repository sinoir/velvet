package com.delectable.mobile.api.endpointmodels;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseResponse implements Parcelable {

    private boolean e_tag_match;

    private String e_tag;

    private String context;

    private boolean success;

    private Error error;

    public String getETag() {
        return e_tag;
    }

    public boolean isETagMatch() {
        return e_tag_match;
    }

    public String getContext() {
        return context;
    }

    public boolean isSuccess() {
        return success;
    }

    public Error getError() {
        return error;
    }



    @Override
    public String toString() {
        return "BaseResponse{" +
                "e_tag_match=" + e_tag_match +
                ", e_tag='" + e_tag + '\'' +
                ", context='" + context + '\'' +
                ", success=" + success +
                ", error=" + error +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(e_tag_match ? (byte) 1 : (byte) 0);
        dest.writeString(this.e_tag);
        dest.writeString(this.context);
        dest.writeByte(success ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.error, 0);
    }

    public BaseResponse() {
    }

    protected BaseResponse(Parcel in) {
        this.e_tag_match = in.readByte() != 0;
        this.e_tag = in.readString();
        this.context = in.readString();
        this.success = in.readByte() != 0;
        this.error = in.readParcelable(Error.class.getClassLoader());
    }

    public static class Error implements Parcelable {

        private int code;

        private String message;

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.code);
            dest.writeString(this.message);
        }

        private Error(Parcel in) {
            this.code = in.readInt();
            this.message = in.readString();
        }

        public static final Parcelable.Creator<Error> CREATOR = new Parcelable.Creator<Error>() {
            public Error createFromParcel(Parcel source) {
                return new Error(source);
            }

            public Error[] newArray(int size) {
                return new Error[size];
            }
        };
    }

}
