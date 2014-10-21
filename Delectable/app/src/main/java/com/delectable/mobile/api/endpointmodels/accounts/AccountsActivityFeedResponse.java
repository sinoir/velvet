package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.api.models.BaseListingResponse;

public class AccountsActivityFeedResponse extends BaseResponse {

    private BaseListingResponse<ActivityRecipient> payload;

    public BaseListingResponse<ActivityRecipient> getPayload() {
        return payload;
    }
}
