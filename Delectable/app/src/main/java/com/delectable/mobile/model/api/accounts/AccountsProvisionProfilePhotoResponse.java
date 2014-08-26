package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.model.api.BaseResponse;

public class AccountsProvisionProfilePhotoResponse extends BaseResponse {

    private ProvisionCapture payload;

    public ProvisionCapture getPayload() {
        return payload;
    }
}
