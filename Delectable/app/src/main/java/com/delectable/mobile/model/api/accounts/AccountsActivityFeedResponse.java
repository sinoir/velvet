package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.api.BaseResponse;

public class AccountsActivityFeedResponse extends BaseResponse {

    private ListingResponse<ActivityRecipient> payload;

    public ListingResponse<ActivityRecipient> getPayload() {
        return payload;
    }
}
