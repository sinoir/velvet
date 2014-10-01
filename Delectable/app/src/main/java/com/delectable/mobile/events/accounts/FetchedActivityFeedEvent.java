package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.events.BaseEvent;

public class FetchedActivityFeedEvent extends BaseEvent {

    private ListingResponse<ActivityRecipient> mListingResponse;

    public FetchedActivityFeedEvent(ListingResponse<ActivityRecipient> listingResponse) {
        super(true);
        mListingResponse = listingResponse;
    }

    public FetchedActivityFeedEvent(String errorMessage) {
        super(errorMessage);
    }

    public ListingResponse<ActivityRecipient> getListingResponse() {
        return mListingResponse;
    }
}
