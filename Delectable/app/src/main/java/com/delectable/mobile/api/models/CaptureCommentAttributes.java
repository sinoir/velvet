package com.delectable.mobile.api.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class CaptureCommentAttributes {

    public static final String TYPE_HASHTAG = "hashtag";

    public static final String TYPE_MENTION = "mention";

    public static final int INDEX_RANGE_START = 0;

    public static final int INDEX_RANGE_LENGTH = 1;

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
}
