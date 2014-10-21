package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.models.ActivityFeedItem;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.events.BaseEvent;

public class FetchedActivityFeedEvent extends BaseEvent {

    private Listing<ActivityFeedItem> mListingResponse;

    public FetchedActivityFeedEvent(Listing<ActivityFeedItem> listingResponse) {
        super(true);
        mListingResponse = listingResponse;
    }

    public FetchedActivityFeedEvent(String errorMessage) {
        super(errorMessage);
    }

    public Listing<ActivityFeedItem> getListingResponse() {
        return mListingResponse;
    }
}
