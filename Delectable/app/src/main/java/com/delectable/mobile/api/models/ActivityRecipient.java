package com.delectable.mobile.api.models;

import java.util.ArrayList;

public class ActivityRecipient extends BaseListingElement {

    private String text;

    private LinkObject selection_link;

    private ArrayList<RangeLink> text_links;

    private PhotoLink right_image_link;

    private PhotoLink left_image_link;

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

    public static class LinkObject {

        String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class PhotoLink extends LinkObject {

        PhotoHash photo;

        public PhotoHash getPhoto() {
            return photo;
        }

        public void setPhoto(PhotoHash photo) {
            this.photo = photo;
        }
    }

    public static class RangeLink extends LinkObject {

        ArrayList<Integer> range;

        public ArrayList<Integer> getRange() {
            return range;
        }

        public void setRange(ArrayList<Integer> range) {
            this.range = range;
        }
    }
}
