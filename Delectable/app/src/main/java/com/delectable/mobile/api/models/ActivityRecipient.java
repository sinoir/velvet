package com.delectable.mobile.api.models;

import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityRecipient extends BaseListingElement {

    String text;

    LinkObject selection_link;

    ArrayList<RangeLink> text_links;

    PhotoLink right_image_link;

    PhotoLink left_image_link;

    @Override
    public BaseResponse buildFromJson(JSONObject jsonObj) {
        return null;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LinkObject getSelectionLink() {
        return selection_link;
    }

    public void setSelectionLink(LinkObject selection_link) {
        this.selection_link = selection_link;
    }

    public ArrayList<RangeLink> getTextLinks() {
        return text_links;
    }

    public void setTextLinks(ArrayList<RangeLink> text_links) {
        this.text_links = text_links;
    }

    public PhotoLink getRightImageLink() {
        return right_image_link;
    }

    public void setRightImageLink(PhotoLink right_image_link) {
        this.right_image_link = right_image_link;
    }

    public PhotoLink getLeftImageLink() {
        return left_image_link;
    }

    public void setLeftImageLink(PhotoLink left_image_link) {
        this.left_image_link = left_image_link;
    }

    public class LinkObject {

        String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class PhotoLink extends LinkObject {

        PhotoHash photo;

        public PhotoHash getPhoto() {
            return photo;
        }

        public void setPhoto(PhotoHash photo) {
            this.photo = photo;
        }
    }

    public class RangeLink extends LinkObject {

        ArrayList<Integer> range;

        public ArrayList<Integer> getRange() {
            return range;
        }

        public void setRange(ArrayList<Integer> range) {
            this.range = range;
        }
    }
}
