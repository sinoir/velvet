package com.delectable.mobile.events.foursquare;

import com.delectable.mobile.events.BaseEvent;
import com.delectable.mobile.model.api.foursquare.FoursquareVenueItem;

import java.util.List;

public class SearchedFoursquareVenuesEvent extends BaseEvent {

    private List<FoursquareVenueItem> mVenues;

    public SearchedFoursquareVenuesEvent(List<FoursquareVenueItem> venues) {
        super(true);
        mVenues = venues;
    }

    public SearchedFoursquareVenuesEvent(String errorMessage) {
        super(errorMessage);
    }

    public SearchedFoursquareVenuesEvent(boolean isSuccessful) {
        super(isSuccessful);
    }

    public List<FoursquareVenueItem> getVenues() {
        return mVenues;
    }
}
