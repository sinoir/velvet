package com.delectable.mobile.tests;

import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class WineSourceModelTest extends BaseInstrumentationTestCase {

    private WineSourceModel mWineSourceModel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mWineSourceModel = new WineSourceModel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mWineSourceModel = null;
    }

    private WineProfilesSourceResponse.PayLoad loadWineSourcePayload() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_wine_source);
        WineProfilesSourceResponse response = mGson
                .fromJson(json.toString(), WineProfilesSourceResponse.class);
        return response.getPayload();
    }

    public void testSaveWineSource() throws JSONException {
        WineProfilesSourceResponse.PayLoad expectedWineSource = loadWineSourcePayload();

        mWineSourceModel.saveWineSource(expectedWineSource);
        assertEquals(mWineSourceModel.getWineSourceByWineIdMap()
                .get(expectedWineSource.getWineProfile().getId()), expectedWineSource);
    }

    public void testGetPurchaseOffer() throws JSONException {
        WineProfilesSourceResponse.PayLoad expectedWineSource = loadWineSourcePayload();
        String wineId = expectedWineSource.getWineProfile().getId();

        mWineSourceModel.saveWineSource(expectedWineSource);
        assertEquals(mWineSourceModel.getPurchaseOffer(wineId),
                expectedWineSource.getPurchaseOffer());
    }

    public void testGetMinWineWithPrice() throws JSONException {
        WineProfilesSourceResponse.PayLoad expectedWineSource = loadWineSourcePayload();
        String wineId = expectedWineSource.getWineProfile().getId();

        mWineSourceModel.saveWineSource(expectedWineSource);
        assertEquals(mWineSourceModel.getMinWineWithPrice(
                wineId), expectedWineSource.getWineProfile());
    }
}
