package com.delectable.mobile.model.local;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;

import java.util.ArrayList;

/**
 * Listing object storing IDs for specific objects
 */
public class ListingObject {

    String e_tag;

    boolean more;

    ListingResponse.Boundaries boundaries;

    ArrayList<String> object_ids;

    public ListingObject(ListingResponse<CaptureDetails> captureDetails) {
        e_tag = captureDetails.getETag();
        more = captureDetails.getMore();
        boundaries = captureDetails.getBoundaries();
        object_ids = new ArrayList<String>();
        object_ids.addAll(captureDetails.getAllIds());
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    public boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public ListingResponse.Boundaries getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(ListingResponse.Boundaries boundaries) {
        this.boundaries = boundaries;
    }

    public ArrayList<String> getObjectIds() {
        return object_ids;
    }

    public void setObjectIds(ArrayList<String> objectIds) {
        this.object_ids = objectIds;
    }

    @Override
    public String toString() {
        return "ListingObject{" +
                "e_tag='" + e_tag + '\'' +
                ", more=" + more +
                ", boundaries=" + boundaries +
                ", object_ids=" + object_ids +
                '}';
    }
}
