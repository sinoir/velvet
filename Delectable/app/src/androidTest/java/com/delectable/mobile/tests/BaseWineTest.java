package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.BaseSearch;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.SearchHit;

import org.json.JSONException;
import org.json.JSONObject;

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
        BaseSearch searchQuery = new BaseSearch();
        JSONObject json = loadJsonObjectFromResource(R.raw.test_base_wine_search_min_ctx);
        BaseSearch actualSearchResult = (BaseSearch) searchQuery
                .parsePayloadForAction(json, BaseSearch.A_BASE_WINE_SEARCH);

        assertEquals("Napa Valley", actualSearchResult.getQ());
        assertEquals(0, actualSearchResult.getOffset().intValue());
        assertEquals(2, actualSearchResult.getLimit().intValue());
        assertEquals(234, actualSearchResult.getSearchTime().intValue());
        assertEquals(1395, actualSearchResult.getTotal().intValue());

        assertEquals(2, actualSearchResult.getHits().size());
        SearchHit<BaseWine> actualFirstHit = actualSearchResult.getHits().get(0);
        assertEquals(154.30392, actualFirstHit.getScore());
        assertEquals("base_wine", actualFirstHit.getType());

        BaseWine actualFirstBaseWine = actualFirstHit.getObject();
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
}
