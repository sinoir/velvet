package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceResponse;
import com.delectable.mobile.api.models.PurchaseOffer;
import com.delectable.mobile.api.models.WineProfileMinimal;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WineSourceModel {

    private static final String TAG = WineSourceModel.class.getSimpleName();

    private HashMap<String, WineProfilesSourceResponse.PayLoad> mWineSourceByWineId
            = new HashMap<String, WineProfilesSourceResponse.PayLoad>();

    @Inject
    public WineSourceModel() {
    }

    public void clear() {
        mWineSourceByWineId.clear();
    }

    public PurchaseOffer getPurchaseOffer(String wineId) {
        if (!mWineSourceByWineId.containsKey(wineId)) {
            return null;
        }
        return mWineSourceByWineId.get(wineId).getPurchaseOffer();
    }

    public WineProfileMinimal getMinWineWithPrice(String wineId) {
        if (!mWineSourceByWineId.containsKey(wineId)) {
            return null;
        }
        return mWineSourceByWineId.get(wineId).getWineProfile();
    }

    public void saveWineSource(WineProfilesSourceResponse.PayLoad wineSource) {
        if (wineSource == null) {
            return;
        }
        mWineSourceByWineId.put(wineSource.getWineProfile().getId(), wineSource);
    }

    /**
     * DO NOT USE DIRECTLY -> used for tests
     */
    public HashMap<String, WineProfilesSourceResponse.PayLoad> getWineSourceByWineIdMap() {
        return mWineSourceByWineId;
    }
}
