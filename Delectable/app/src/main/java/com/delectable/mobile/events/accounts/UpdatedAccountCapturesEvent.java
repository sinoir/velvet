package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.events.BaseEvent;

/**
 * Designed to inform when the ListingResponse has been retrieve and combined into the current list
 * being displayed in UI. The Job+Cache mimics the API, and will return a ListingResponse with just
 * an updates array. This will always be the case, and the implementer will not need to combine
 * their ListingResponse into their current capturesList.
 *
 * <br>Sometimes the listing may be null with a successful request. This could mean:
 *
 * <br>maybe user has no captures
 *
 * <br>maybe list is completely up to date and e_tag_match is true (in the response wrapper for the
 * listing payload).
 */
public class UpdatedAccountCapturesEvent extends BaseEvent {

    private String mAccountId;

    private BaseListingResponse<CaptureDetails> mListing;

    public UpdatedAccountCapturesEvent(String accountId,
            BaseListingResponse<CaptureDetails> listing) {
        super(true);
        mAccountId = accountId;
        mListing = listing;
    }

    public UpdatedAccountCapturesEvent(String errorMessage) {
        super(errorMessage);
    }

    public String getAccountId() {
        return mAccountId;
    }

    public BaseListingResponse<CaptureDetails> getListing() {
        return mListing;
    }
}
