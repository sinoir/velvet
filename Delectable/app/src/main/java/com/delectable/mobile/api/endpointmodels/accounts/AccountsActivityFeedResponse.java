package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.ActivityFeedItem;
import com.delectable.mobile.api.models.BaseListingResponse;

public class AccountsActivityFeedResponse extends BaseResponse {

    private BaseListingResponse<ActivityFeedItem> payload;

    public BaseListingResponse<ActivityFeedItem> getPayload() {
        return payload;
    }
}
