package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.api.models.ListingResponse;

public class AccountsActivityFeedResponse extends BaseResponse {

    private ListingResponse<ActivityRecipient> payload;

    public ListingResponse<ActivityRecipient> getPayload() {
        return payload;
    }
}
