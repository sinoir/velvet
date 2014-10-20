package com.delectable.mobile.api.endpointmodels.wineprofiles;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.WineProfile;

public class WineProfilesModifyWishlistResponse extends BaseResponse {

    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {

        private WineProfile wineProfile;

        public WineProfile getWineProfile() {
            return wineProfile;
        }
    }
}
