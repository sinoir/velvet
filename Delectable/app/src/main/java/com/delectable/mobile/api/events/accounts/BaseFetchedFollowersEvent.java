package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;

/**
 * Sometimes the followerListing may be null with a successful request. This could mean: <br>maybe
 * user has no followers <br>maybe list is completely up to date and e_tag_match is true (in the
 * response wrapper for the listing payload)
 */
public class BaseFetchedFollowersEvent extends BaseEvent {

    private String mAccountId;

    private BaseListingResponse<AccountMinimal> mListing;

    private boolean mInvalidate;

    public BaseFetchedFollowersEvent(String accountId, BaseListingResponse<AccountMinimal> listing,
            boolean invalidate) {
        super(true);
        mAccountId = accountId;
        mListing = listing;
        mInvalidate = invalidate;
    }

    public BaseFetchedFollowersEvent(String errorMessage) {
        super(errorMessage);
    }

    public String getAccountId() {
        return mAccountId;
    }

    public BaseListingResponse<AccountMinimal> getListing() {
        return mListing;
    }

    public boolean isInvalidate() {
        return mInvalidate;
    }
}
