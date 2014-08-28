package com.delectable.mobile.model.api.scanwinelabels;

import com.delectable.mobile.api.models.ProvisionCapture;
import com.delectable.mobile.model.api.BaseResponse;

public class ProvisionPhotoResponse extends BaseResponse {

    private ProvisionCapture payload;

    public ProvisionCapture getPayload() {
        return payload;
    }

}
