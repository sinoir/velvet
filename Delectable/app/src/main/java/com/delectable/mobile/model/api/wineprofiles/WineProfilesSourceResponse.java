package com.delectable.mobile.model.api.wineprofiles;

import com.delectable.mobile.api.models.PurchaseOffer;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.api.models.WineProfileMinimal;
import com.delectable.mobile.model.api.BaseResponse;

public class WineProfilesSourceResponse extends BaseResponse {

    private PayLoad payload;

    public PayLoad getPayload() {
        return payload;
    }

    public static class PayLoad {

        private PurchaseOffer purchase_offer;

        private WineProfileMinimal wine_profile;

        public PurchaseOffer getPurchaseOffer() {
            return purchase_offer;
        }

        public WineProfileMinimal getWineProfile() {
            return wine_profile;
        }
    }
}
