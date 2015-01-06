package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class CaptureCommentAttributes implements Parcelable {

    public static final String TYPE_HASHTAG = "hashtag";

    public static final String TYPE_MENTION = "mention";

    String type;

    ArrayList<Integer> range;

    String id;

    String link;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Integer> getRange() {
        return range;
    }

    public void setRange(ArrayList<Integer> range) {
        this.range = range;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        try {
            return URLDecoder.decode(link, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return link;
        }
    }

    public void setLink(String link) {
        try {
            this.link = URLDecoder.decode(link, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            this.link = link;
        }
    }

    @Override
    public String toString() {
        return "CaptureCommentAttributes{" +
                "type='" + type + '\'' +
                ", range=" + range +
                ", id='" + id + '\'' +
                ", link='" + link + '\'' +
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

        CaptureCommentAttributes that = (CaptureCommentAttributes) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(type);
        out.writeList(range);
        out.writeString(id);
        out.writeString(link);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator<CaptureCommentAttributes> CREATOR
            = new Parcelable.Creator<CaptureCommentAttributes>() {
        public CaptureCommentAttributes createFromParcel(Parcel in) {
            return new CaptureCommentAttributes(in);
        }

        public CaptureCommentAttributes[] newArray(int size) {
            return new CaptureCommentAttributes[size];
        }
    };

    @SuppressWarnings("unchecked")
    private CaptureCommentAttributes(Parcel in) {
        type = in.readString();
        range = in.readArrayList(Integer.class.getClassLoader());
        id = in.readString();
        link = in.readString();
    }
}
