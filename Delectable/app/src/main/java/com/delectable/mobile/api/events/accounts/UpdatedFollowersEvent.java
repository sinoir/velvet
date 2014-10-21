package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.Listing;

/**
 * Sometimes the followerListing may be null with a successful request. This could mean: <br>maybe
 * user has no followers <br>maybe list is completely up to date and e_tag_match is true (in the
 * response wrapper for the listing payload)
 */
public class UpdatedFollowersEvent extends BaseFetchedFollowersEvent {

    public UpdatedFollowersEvent(String accountId, Listing<AccountMinimal> listing,
            boolean invalidate) {
        super(accountId, listing, invalidate);
    }

    public UpdatedFollowersEvent(String errorMessage) {
        super(errorMessage);
    }
}
