package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class CaptureCommentAttributes {

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
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
}
