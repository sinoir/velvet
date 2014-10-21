package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.models.ActivityFeedItem;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.events.BaseEvent;

public class FetchedActivityFeedEvent extends BaseEvent {

    private BaseListingResponse<ActivityFeedItem> mListingResponse;

    public FetchedActivityFeedEvent(BaseListingResponse<ActivityFeedItem> listingResponse) {
        super(true);
        mListingResponse = listingResponse;
    }

    public FetchedActivityFeedEvent(String errorMessage) {
        super(errorMessage);
    }

    public BaseListingResponse<ActivityFeedItem> getListingResponse() {
        return mListingResponse;
    }
}
