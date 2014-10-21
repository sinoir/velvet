package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.api.models.SearchResult;
import com.delectable.mobile.api.endpointmodels.basewines.BaseWinesSearchResponse;
import com.delectable.mobile.api.endpointmodels.basewines.BaseWineResponse;
import com.delectable.mobile.api.models.WineProfileSubProfile;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;

public class BaseWineTest extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseBaseWineSearchResults() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_base_wine_search_min_ctx);
        BaseWinesSearchResponse response = mGson
                .fromJson(json.toString(), BaseWinesSearchResponse.class);
        SearchResult<BaseWineMinimal> payload = response.getPayload();

        assertEquals("Napa Valley", payload.getQ());
        assertEquals(0, payload.getOffset());
        assertEquals(2, payload.getLimit());
        assertEquals(234, payload.getSearchTime());
        assertEquals(1395, payload.getTotal());

        assertEquals(2, payload.getHits().size());
        SearchHit<BaseWineMinimal> actualFirstHit = payload.getHits().get(0);
        assertEquals(154.30392, actualFirstHit.getScore());
        assertEquals("base_wine", actualFirstHit.getType());

        BaseWineMinimal actualFirstBaseWine = actualFirstHit.getObject();
        assertEquals("5305ba538953f6d73900543d", actualFirstBaseWine.getId());
        assertEquals("Napa Valley Pinot Noir", actualFirstBaseWine.getName());
        assertEquals("Napa Ridge", actualFirstBaseWine.getProducerName());
        assertEquals("minimal", actualFirstBaseWine.getContext());
        assertEquals("7QS0YY6tCgAgMw", actualFirstBaseWine.getETag());

        PhotoHash actualPhotoHash = actualFirstBaseWine.getPhoto();
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f.jpg",
                actualPhotoHash.getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_nano.jpg",
                actualPhotoHash.getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_micro.jpg",
                actualPhotoHash.getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_thumb.jpg",
                actualPhotoHash.getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_250x250.jpg",
                actualPhotoHash.get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_medium.jpg",
                actualPhotoHash.getMediumUrl());
    }

    public void testParseBaseWineProfile() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_base_wine_profile_ctx);

        BaseWineResponse responseObject = mGson.fromJson(json.toString(), BaseWineResponse.class);
        BaseWine actualBaseWine = responseObject.getBaseWine();

        assertEquals("5305ba538953f6d73900543d", actualBaseWine.getId());
        assertEquals(1, actualBaseWine.getRatingsSummary().getAllCount());
        assertEquals(30.0, actualBaseWine.getRatingsSummary().getAllAvg());
        assertEquals(0, actualBaseWine.getRatingsSummary().getProCount());
        assertEquals(-1.0, actualBaseWine.getRatingsSummary().getProAvg());
        assertEquals("Napa Valley Pinot Noir", actualBaseWine.getName());
        assertEquals("Napa Ridge", actualBaseWine.getProducerName());
        assertEquals("2983852", actualBaseWine.getRegionId());

        assertEquals(3, actualBaseWine.getRegionPath().size());
        assertEquals("2983715", actualBaseWine.getRegionPath().get(0).getId());
        assertEquals("USA", actualBaseWine.getRegionPath().get(0).getName());
        assertEquals("2945729", actualBaseWine.getRegionPath().get(1).getId());
        assertEquals("California", actualBaseWine.getRegionPath().get(1).getName());
        assertEquals("2983852", actualBaseWine.getRegionPath().get(2).getId());
        assertEquals("Napa Valley", actualBaseWine.getRegionPath().get(2).getName());

        assertEquals(1, actualBaseWine.getVarietalComposition().size());
        assertEquals("75", actualBaseWine.getVarietalComposition().get(0).getId());
        assertEquals("Pinot Noir", actualBaseWine.getVarietalComposition().get(0).getName());
        assertEquals("#ecc0b5", actualBaseWine.getVarietalComposition().get(0).getColor());
        assertEquals("50e86605a6d027d09d00025a", actualBaseWine.getDefaultWineProfileId());

        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f.jpg",
                actualBaseWine.getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_nano.jpg",
                actualBaseWine.getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_micro.jpg",
                actualBaseWine.getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_thumb.jpg",
                actualBaseWine.getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_250x250.jpg",
                actualBaseWine.getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_medium.jpg",
                actualBaseWine.getPhoto().getMediumUrl());

        assertEquals("", actualBaseWine.getDescription());
        assertEquals("profile", actualBaseWine.getContext());
        assertEquals("tmC2M7VCWpKZ4g", actualBaseWine.getETag());

        assertEquals(1, actualBaseWine.getWineProfiles().size());
        WineProfileSubProfile firstWineProfile = actualBaseWine.getWineProfiles().get(0);
        assertEquals("50e86605a6d027d09d00025a", firstWineProfile.getId());
        assertEquals(1, firstWineProfile.getRatingsSummary().getAllCount());
        assertEquals(30.0, firstWineProfile.getRatingsSummary().getAllAvg());
        assertEquals(0, firstWineProfile.getRatingsSummary().getProCount());
        assertEquals(-1.0, firstWineProfile.getRatingsSummary().getProAvg());
        assertEquals("2009", firstWineProfile.getVintage());
        assertEquals("5305ba538953f6d73900543d", firstWineProfile.getBaseWineId());
        assertEquals(21.82, firstWineProfile.getPrice());
        assertEquals("confirmed", firstWineProfile.getPriceStatus());
        assertEquals("subprofile", firstWineProfile.getContext());
        assertEquals("XQr5adP35129_Q", firstWineProfile.getETag());
        assertEquals("", firstWineProfile.getDescription());
        assertEquals("Buy it - $21", firstWineProfile.getPriceText());
    }

    public void testBaseWineMinimalParcelable() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_base_wine_profile_ctx);
        BaseWineResponse responseObject = mGson.fromJson(json.toString(), BaseWineResponse.class);
        BaseWineMinimal expectedBaseWine = responseObject.getBaseWine();

        Parcel testParcel = Parcel.obtain();
        expectedBaseWine.writeToParcel(testParcel, 0);

        // Must reset Data position!!
        testParcel.setDataPosition(0);

        BaseWineMinimal actualBaseWine = BaseWineMinimal.CREATOR.createFromParcel(testParcel);

        assertEquals("5305ba538953f6d73900543d", actualBaseWine.getId());
        assertEquals("Napa Valley Pinot Noir", actualBaseWine.getName());
        assertEquals("Napa Ridge", actualBaseWine.getProducerName());

        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f.jpg",
                actualBaseWine.getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_nano.jpg",
                actualBaseWine.getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_micro.jpg",
                actualBaseWine.getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_thumb.jpg",
                actualBaseWine.getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_250x250.jpg",
                actualBaseWine.getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_medium.jpg",
                actualBaseWine.getPhoto().getMediumUrl());

        assertEquals("profile", actualBaseWine.getContext());
        assertEquals("tmC2M7VCWpKZ4g", actualBaseWine.getETag());
    }


    public void testBaseWineParcelable() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_base_wine_profile_ctx);
        BaseWineResponse responseObject = mGson.fromJson(json.toString(), BaseWineResponse.class);
        BaseWine expectedBaseWine = responseObject.getBaseWine();

        Parcel testParcel = Parcel.obtain();
        expectedBaseWine.writeToParcel(testParcel, 0);

        // Must reset Data position!!
        testParcel.setDataPosition(0);

        BaseWine actualBaseWine = BaseWine.CREATOR.createFromParcel(testParcel);

        assertEquals("5305ba538953f6d73900543d", actualBaseWine.getId());
        assertEquals(1, actualBaseWine.getRatingsSummary().getAllCount());
        assertEquals(30.0, actualBaseWine.getRatingsSummary().getAllAvg());
        assertEquals(0, actualBaseWine.getRatingsSummary().getProCount());
        assertEquals(-1.0, actualBaseWine.getRatingsSummary().getProAvg());
        assertEquals("Napa Valley Pinot Noir", actualBaseWine.getName());
        assertEquals("Napa Ridge", actualBaseWine.getProducerName());
        assertEquals("2983852", actualBaseWine.getRegionId());

        assertEquals(3, actualBaseWine.getRegionPath().size());
        assertEquals("2983715", actualBaseWine.getRegionPath().get(0).getId());
        assertEquals("USA", actualBaseWine.getRegionPath().get(0).getName());
        assertEquals("2945729", actualBaseWine.getRegionPath().get(1).getId());
        assertEquals("California", actualBaseWine.getRegionPath().get(1).getName());
        assertEquals("2983852", actualBaseWine.getRegionPath().get(2).getId());
        assertEquals("Napa Valley", actualBaseWine.getRegionPath().get(2).getName());

        assertEquals(1, actualBaseWine.getVarietalComposition().size());
        assertEquals("75", actualBaseWine.getVarietalComposition().get(0).getId());
        assertEquals("Pinot Noir", actualBaseWine.getVarietalComposition().get(0).getName());
        assertEquals("#ecc0b5", actualBaseWine.getVarietalComposition().get(0).getColor());
        assertEquals("50e86605a6d027d09d00025a", actualBaseWine.getDefaultWineProfileId());

        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f.jpg",
                actualBaseWine.getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_nano.jpg",
                actualBaseWine.getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_micro.jpg",
                actualBaseWine.getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_thumb.jpg",
                actualBaseWine.getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_250x250.jpg",
                actualBaseWine.getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_medium.jpg",
                actualBaseWine.getPhoto().getMediumUrl());

        assertEquals("", actualBaseWine.getDescription());
        assertEquals("profile", actualBaseWine.getContext());
        assertEquals("tmC2M7VCWpKZ4g", actualBaseWine.getETag());

        assertEquals(1, actualBaseWine.getWineProfiles().size());
        WineProfileSubProfile firstWineProfile = actualBaseWine.getWineProfiles().get(0);
        assertEquals("50e86605a6d027d09d00025a", firstWineProfile.getId());
        assertEquals(1, firstWineProfile.getRatingsSummary().getAllCount());
        assertEquals(30.0, firstWineProfile.getRatingsSummary().getAllAvg());
        assertEquals(0, firstWineProfile.getRatingsSummary().getProCount());
        assertEquals(-1.0, firstWineProfile.getRatingsSummary().getProAvg());
        assertEquals("2009", firstWineProfile.getVintage());
        assertEquals("5305ba538953f6d73900543d", firstWineProfile.getBaseWineId());
        assertEquals(21.82, firstWineProfile.getPrice());
        assertEquals("confirmed", firstWineProfile.getPriceStatus());
        assertEquals("subprofile", firstWineProfile.getContext());
        assertEquals("XQr5adP35129_Q", firstWineProfile.getETag());
        assertEquals("", firstWineProfile.getDescription());
        assertEquals("Buy it - $21", firstWineProfile.getPriceText());
    }
}
