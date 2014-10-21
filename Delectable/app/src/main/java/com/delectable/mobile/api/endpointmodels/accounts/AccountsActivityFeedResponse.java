package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.ActivityFeedItem;
import com.delectable.mobile.api.models.Listing;

public class AccountsActivityFeedResponse extends BaseResponse {

    private Listing<ActivityFeedItem> payload;

    public Listing<ActivityFeedItem> getPayload() {
        return payload;
    }
}
