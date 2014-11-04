package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.PurchaseOffer;
import com.delectable.mobile.api.models.WineProfileMinimal;
import com.delectable.mobile.api.endpointmodels.wineprofiles.WineProfilesSourceResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class WineSourceTest extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParsedWineSource() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_wine_source);
        WineProfilesSourceResponse response = mGson
                .fromJson(json.toString(), WineProfilesSourceResponse.class);
        WineProfilesSourceResponse.PayLoad actualSource = response.getPayload();

        PurchaseOffer actualPurchaseOffer = actualSource.getPurchaseOffer();
        assertEquals("5384fc9a753490ae0d00013b", actualPurchaseOffer.getId());
        assertEquals("2009", actualPurchaseOffer.getVintage());
        assertEquals(1.401408E9f, actualPurchaseOffer.getExpiration());
        assertEquals(1, actualPurchaseOffer.getMinQuant());
        assertEquals(12, actualPurchaseOffer.getMaxQuant());
        assertEquals(1, actualPurchaseOffer.getDefaultQuant());

        assertEquals(12, actualPurchaseOffer.getPricing().size());
        assertEquals(1, actualPurchaseOffer.getPricing().get(0).getQuantity().intValue());
        assertEquals("$21.82", actualPurchaseOffer.getPricing().get(0).getPerBbottle());
        assertEquals("$21.82", actualPurchaseOffer.getPricing().get(0).getWine());
        assertEquals("$22.00", actualPurchaseOffer.getPricing().get(0).getShipping());
        assertEquals("$0.00", actualPurchaseOffer.getPricing().get(0).getTax());
        assertEquals("$43.82", actualPurchaseOffer.getPricing().get(0).getTotal());

        assertEquals("bottle", actualPurchaseOffer.getObjectSingularNoun());
        assertEquals("bottles", actualPurchaseOffer.getObjectPluralNoun());
        assertEquals("Order 12 bottles to get 1 cent shipping!",
                actualPurchaseOffer.getMarketingMessage());

        WineProfileMinimal actualWine = actualSource.getWineProfile();
        assertEquals("50e86605a6d027d09d00025a", actualWine.getId());
        assertEquals("2009", actualWine.getVintage());
        assertEquals("Napa Ridge", actualWine.getProducerName());
        assertEquals("Napa Valley Pinot Noir", actualWine.getName());
        assertEquals("5305ba538953f6d73900543d", actualWine.getBaseWineId());
        assertEquals(21.82, actualWine.getPrice());
        assertEquals("confirmed", actualWine.getPriceStatus());

        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f.jpg",
                actualWine.getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_nano.jpg",
                actualWine.getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_micro.jpg",
                actualWine.getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_thumb.jpg",
                actualWine.getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_250x250.jpg",
                actualWine.getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_medium.jpg",
                actualWine.getPhoto().getMediumUrl());
        assertEquals("minimal", actualWine.getContext());
        assertEquals("SF1craz1xak8Uw", actualWine.getETag());
        assertEquals("", actualWine.getDescription());
        assertEquals("Buy it - $21", actualWine.getPriceText());
    }

}
