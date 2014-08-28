package com.delectable.mobile.model.api.scanwinelabels;

import com.google.gson.annotations.SerializedName;

import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.model.api.BaseRequest;

import java.io.Serializable;
import java.util.List;

public class AddCaptureFromPendingCaptureRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = -4659135797231085938L;

    private Payload payload;

    public AddCaptureFromPendingCaptureRequest(String pendingCaptureId) {
        this.payload = new Payload();
        this.payload.pending_capture_id = pendingCaptureId;
    }

    public AddCaptureFromPendingCaptureRequest(String context, String e_tag,
            Payload payload) {
        super(context, e_tag);
        this.payload = payload;
    }

    public void setPendingCaptureId(String pending_capture_id) {
        this.payload = new Payload();
        this.payload.pending_capture_id = pending_capture_id;
    }

    public void setPrivate(Boolean private_) {
        this.payload.private_ = private_;
    }

    public void setRating(int rating) {
        this.payload.rating = rating;
    }

    public void setNote(String note) {
        this.payload.note = note;
    }

    public void setShareTw(Boolean share_tw) {
        this.payload.share_tw = share_tw;
    }

    public void setUserTw(String user_tw) {
        this.payload.user_tw = user_tw;
    }

    public void setShareFb(Boolean share_fb) {
        this.payload.share_fb = share_fb;
    }

    public void setUserCountryCode(String user_country_code) {
        this.payload.user_country_code = user_country_code;
    }

    public void setFoursquareLocationId(String foursquare_location_id) {
        this.payload.foursquare_location_id = foursquare_location_id;
    }

    public void setTaggees(List<TaggeeContact> taggees) {
        this.payload.taggees = taggees;
    }

    @Override
    public String toString() {
        return "AddCaptureFromPendingCaptureRequest{" +
                "payload=" + payload +
                '}';
    }

    public static class Payload implements Serializable {

        private static final long serialVersionUID = -8045685864917603357L;

        private String pending_capture_id;

        @SerializedName("private")
        private boolean private_;

        private int rating;

        private String note;

        private boolean share_tw;

        private String user_tw; // Only required if share_tw it True

        private boolean share_fb;

        private String user_country_code;

        private String foursquare_location_id;

        private List<TaggeeContact> taggees;

        public Payload() {
        }

        public Payload(String pending_capture_id, boolean private_, int rating, String note,
                boolean share_tw, String user_tw, boolean share_fb, String user_country_code,
                String foursquare_location_id,
                List<TaggeeContact> taggees) {
            this.pending_capture_id = pending_capture_id;
            this.private_ = private_;
            this.rating = rating;
            this.note = note;
            this.share_tw = share_tw;
            this.user_tw = user_tw;
            this.share_fb = share_fb;
            this.user_country_code = user_country_code;
            this.foursquare_location_id = foursquare_location_id;
            this.taggees = taggees;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "pending_capture_id='" + pending_capture_id + '\'' +
                    ", private_=" + private_ +
                    ", rating=" + rating +
                    ", note='" + note + '\'' +
                    ", share_tw=" + share_tw +
                    ", user_tw='" + user_tw + '\'' +
                    ", share_fb=" + share_fb +
                    ", user_country_code='" + user_country_code + '\'' +
                    ", foursquare_location_id='" + foursquare_location_id + '\'' +
                    ", taggees=" + taggees +
                    '}';
        }
    }
}
