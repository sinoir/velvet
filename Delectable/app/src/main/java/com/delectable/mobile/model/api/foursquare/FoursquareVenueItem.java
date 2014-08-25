package com.delectable.mobile.model.api.foursquare;

public class FoursquareVenueItem {

    private String id;

    private String name;

    private FoursquareLocation location;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        if (location != null && location.address != null) {
            return location.address;
        }
        return null;
    }

    public static class FoursquareLocation {

        private String address;
    }

}
