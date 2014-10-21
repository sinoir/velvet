package com.delectable.mobile.tests;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.endpointmodels.BaseListingWrapperResponse;
import com.delectable.mobile.api.cache.localmodels.CacheListing;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class CaptureDetailsListingIOTestCase extends BaseInstrumentationTestCase {

    private Listing<CaptureDetails> mFollowFeedListing;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_details_ctx);
        Type type = new TypeToken<BaseListingWrapperResponse<CaptureDetails>>() {
        }.getType();
        BaseListingWrapperResponse<CaptureDetails> feedResponseObject = mGson
                .fromJson(json.toString(), type);
        mFollowFeedListing = feedResponseObject.getPayload();
    }

    @Override
    protected void tearDown() throws Exception {
        mFollowFeedListing = null;
        super.tearDown();
    }

    public void testCombineListingObjectFromListingResponse() throws JSONException {
        CacheListing listingObject = new CacheListing(mFollowFeedListing);
        assertEquals(mFollowFeedListing.getBoundaries(), listingObject.getBoundaries());
        assertEquals(mFollowFeedListing.getETag(), listingObject.getETag());
        assertEquals(mFollowFeedListing.getMore(), listingObject.getMore());
    }

    public void testSaveCachedFollowerFeedListingResponse() {
        // TODO: Figure out how to use the Model with Dagger
    }
}
