package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.endpointmodels.BaseResponse;

public class AccountsFollowersResponse extends BaseResponse {

    private BaseListingResponse<AccountMinimal> payload;

    public BaseListingResponse<AccountMinimal> getPayload() {
        return payload;
    }
}
