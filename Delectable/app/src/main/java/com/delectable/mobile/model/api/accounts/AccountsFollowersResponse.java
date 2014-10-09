package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.model.api.BaseResponse;

public class AccountsFollowersResponse extends BaseResponse {

    private BaseListingResponse<AccountMinimal> payload;

    public BaseListingResponse<AccountMinimal> getPayload() {
        return payload;
    }
}
