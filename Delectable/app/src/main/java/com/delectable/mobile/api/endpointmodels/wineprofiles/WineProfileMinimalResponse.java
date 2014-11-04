package com.delectable.mobile.api.endpointmodels.wineprofiles;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.WineProfileMinimal;

public class WineProfileMinimalResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private WineProfileMinimal wineProfile;

        public WineProfileMinimal getWineProfile() {
            return wineProfile;
        }
    }
}
