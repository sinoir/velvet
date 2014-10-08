package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.events.BaseEvent;

/**
 * Sometimes the followerListing may be null with a successful request. This could mean:
 * <br>maybe user has no followers
 * <br>maybe list is completely up to date and e_tag_match is true (in the response wrapper for the listing payload)
 */
public class UpdatedFollowersEvent extends BaseEvent {

    private String mAccountId;

    private BaseListingResponse<AccountMinimal> mListing;

    public UpdatedFollowersEvent(String accountId, BaseListingResponse<AccountMinimal> listing) {
        super(true);
        mAccountId = accountId;
        mListing = listing;
    }

    public UpdatedFollowersEvent(String errorMessage) {
        super(errorMessage);
    }

    public String getAccountId() {
        return mAccountId;
    }

    public BaseListingResponse<AccountMinimal> getFollowerListing() {
        return mListing;
    }
}
