package com.delectable.mobile.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class CaptureCommentAttributes
        implements Parcelable, Serializable, Comparable<CaptureCommentAttributes> {

    public static final String TYPE_HASHTAG = "hashtag";

    public static final String TYPE_MENTION = "mention";

    public static final int INDEX_RANGE_START = 0;

    public static final int INDEX_RANGE_LENGTH = 1;

    String type;

    ArrayList<Integer> range;

    String id;

    String link;

    public CaptureCommentAttributes(String id, String type, int tagStart, int tagLength) {
        this.id = id;
        this.type = type;
        range = new ArrayList<>(2);
        range.add(INDEX_RANGE_START, tagStart);
        range.add(INDEX_RANGE_LENGTH, tagLength);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Integer> getRange() {
        return range;
    }

    public int getStart() {
        return getRange().get(INDEX_RANGE_START);
    }

    public int getEnd() {
        return getStart() + getLength();
    }

    public int getLength() {
        return getRange().get(INDEX_RANGE_LENGTH);
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
        if (link == null) {
            return null;
        }

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

    /**
     * Compares this object to the specified object to determine their relative order. Order is
     * appearance of tag in the text.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another}; a positive integer
     * if this instance is greater than {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something comparable
     *                            to {@code this} instance.
     */
    @Override
    public int compareTo(CaptureCommentAttributes another) {
        return this.getStart() - another.getStart();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
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
