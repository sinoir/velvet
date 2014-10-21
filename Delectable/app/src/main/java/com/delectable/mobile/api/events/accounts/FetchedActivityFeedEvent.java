package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.events.BaseEvent;

public class FetchedActivityFeedEvent extends BaseEvent {

    private BaseListingResponse<ActivityRecipient> mListingResponse;

    public FetchedActivityFeedEvent(BaseListingResponse<ActivityRecipient> listingResponse) {
        super(true);
        mListingResponse = listingResponse;
    }

    public FetchedActivityFeedEvent(String errorMessage) {
        super(errorMessage);
    }

    public BaseListingResponse<ActivityRecipient> getListingResponse() {
        return mListingResponse;
    }
}
