package com.delectable.mobile.api.endpointmodels;

import com.delectable.mobile.api.models.Motd;

public class MotdResponse extends BaseResponse {

    private Motd payload;

    public Motd getPayload() {
        return payload;
    }

}
