package com.delectable.mobile.api.models;

public class DeleteHash {

    private String type;

    private String id;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    /**
     * Don't Modify this. The BaseFetchListingJob depends on this toString() to return the delete
     * id.
     */
    @Override
    public String toString() {
        return id;
    }
}
