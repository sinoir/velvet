package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.api.captures.CaptureFeedResponse;
import com.delectable.mobile.model.local.ListingObject;

import org.json.JSONException;
import org.json.JSONObject;

public class CaptureDetailsListingIOTestCase extends BaseInstrumentationTestCase {

    private ListingResponse<CaptureDetails> mFollowFeedListing;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_details_ctx);
        CaptureFeedResponse feedResponseObject = mGson.fromJson(json.toString(),
                CaptureFeedResponse.class);
        mFollowFeedListing = feedResponseObject.getPayload();
    }

    @Override
    protected void tearDown() throws Exception {
        mFollowFeedListing = null;
        super.tearDown();
    }

    public void testCombineListingObjectFromListingResponse() throws JSONException {
        ListingObject listingObject = new ListingObject(mFollowFeedListing);
        assertEquals(mFollowFeedListing.getBoundaries(), listingObject.getBoundaries());
        assertEquals(mFollowFeedListing.getETag(), listingObject.getETag());
        assertEquals(mFollowFeedListing.getMore(), listingObject.getMore());
        assertEquals(mFollowFeedListing.getAllIds(), listingObject.getObjectIds());
    }

    public void testSaveCachedFollowerFeedListingResponse() {
        // TODO: Figure out how to use the Model with Dagger
    }
}
