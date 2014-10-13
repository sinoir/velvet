package com.delectable.mobile.events;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.IDable;

/**
 * Designed to inform when the ListingResponse has been retrieve and combined into the current list
 * being displayed in UI. The Job+Cache mimics the API, and will return a ListingResponse with just
 * an updates array. This will always be the case, and the implementer will not need to combine
 * their ListingResponse into their current list.
 *
 * <br>Sometimes the listing may be null with a successful request. This could mean:
 *
 * <br>maybe user has no items
 *
 * <br>maybe list is completely up to date and e_tag_match is true (in the response wrapper for the
 * listing payload).
 */
public class UpdatedListingEvent<T extends IDable> extends BaseEvent {

    private String mRequestId;

    private String mAccountId;

    private BaseListingResponse<T> mListing;

    public UpdatedListingEvent(String requestId, String accountId,
            BaseListingResponse<T> listing) {
        super(true);
        mRequestId = requestId;
        mAccountId = accountId;
        mListing = listing;
    }

    public UpdatedListingEvent(String requestId, String accountId, String errorMessage) {
        super(errorMessage);
        mRequestId = requestId;
        mAccountId = accountId;
    }

    public String getRequestId() {
        return mRequestId;
    }

    public String getAccountId() {
        return mAccountId;
    }

    public BaseListingResponse<T> getListing() {
        return mListing;
    }
}
