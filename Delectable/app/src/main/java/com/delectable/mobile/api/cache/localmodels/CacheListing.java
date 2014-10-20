package com.delectable.mobile.api.cache.localmodels;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.BaseListingResponse.Boundaries;
import com.delectable.mobile.api.models.IDable;

import java.util.ArrayList;

/**
 * Stores a list of ids for list items, as well as the information necessary to paginate/update the
 * list.
 */
public class CacheListing<T extends IDable> {

    private String e_tag;

    private boolean more;

    private Boundaries boundaries;

    private ArrayList<String> item_ids;

    public CacheListing(BaseListingResponse<T> listing) {
        e_tag = listing.getETag();
        more = listing.getMore();
        boundaries = listing.getBoundaries();
        item_ids = listing.getUpdatesIds();
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

    public Boundaries getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(Boundaries boundaries) {
        this.boundaries = boundaries;
    }

    public ArrayList<String> getItemIds() {
        return item_ids;
    }

    public void setItemIds(ArrayList<String> itemIds) {
        this.item_ids = itemIds;
    }

    @Override
    public String toString() {
        return "CacheListing{" +
                "e_tag='" + e_tag + '\'' +
                ", more=" + more +
                ", boundaries=" + boundaries +
                ", item_ids=" + item_ids +
                '}';
    }
}
