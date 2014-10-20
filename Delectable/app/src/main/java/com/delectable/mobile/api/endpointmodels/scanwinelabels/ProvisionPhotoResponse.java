package com.delectable.mobile.api.endpointmodels.scanwinelabels;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.ProvisionCapture;

public class ProvisionPhotoResponse extends BaseResponse {

    private ProvisionCapture payload;

    public ProvisionCapture getPayload() {
        return payload;
    }

}
