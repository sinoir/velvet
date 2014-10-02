package com.delectable.mobile.model.api.wineprofiles;

import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.model.api.BaseResponse;

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
