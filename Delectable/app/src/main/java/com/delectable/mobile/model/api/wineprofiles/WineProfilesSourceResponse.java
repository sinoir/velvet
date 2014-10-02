package com.delectable.mobile.model.api.wineprofiles;

import com.delectable.mobile.api.models.WineSource;
import com.delectable.mobile.model.api.BaseResponse;

public class WineProfilesSourceResponse extends BaseResponse {

    private WineSource payload;

    public WineSource getPayload() {
        return payload;
    }
}
