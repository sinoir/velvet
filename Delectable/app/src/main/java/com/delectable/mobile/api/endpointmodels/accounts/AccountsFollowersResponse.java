package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.endpointmodels.BaseResponse;

public class AccountsFollowersResponse extends BaseResponse {

    private Listing<AccountMinimal> payload;

    public Listing<AccountMinimal> getPayload() {
        return payload;
    }
}
