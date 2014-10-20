package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.api.endpointmodels.accounts.AccountsActivityFeedResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityFeedTestCase extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseActivityFeed() throws JSONException {
        // TODO: Update test when we get more non null data , such as getTextLinks() -> need to test the Range bit.
        JSONObject json = loadJsonObjectFromResource(R.raw.test_activity_feed_list);
        String expectedContext = "feed_element";

        AccountsActivityFeedResponse response = mGson
                .fromJson(json.toString(), AccountsActivityFeedResponse.class);
        ListingResponse<ActivityRecipient> actualListing = response.getPayload();

        assertNull(actualListing.getBoundariesFromBefore());
        assertNull(actualListing.getBoundariesFromAfter());
        assertNull(actualListing.getBoundariesFromSince());

        ActivityRecipient actualFirstElement = actualListing.getUpdates().get(0);

        assertEquals(
                "Follow:50f832ae61981432c9000034#51afb3bb0046849653000002.51afb3bb0046849653000002",
                actualListing.getBoundariesToBefore());
        assertEquals(
                "Like:50f832ae61981432c9000034#536af8633ff873c6f000000c.51afb3bb0046849653000002",
                actualListing.getBoundariesToAfter());
        assertEquals("1406240456.7031116", actualListing.getBoundariesToSince());
        assertEquals(0, actualListing.getBefore().size());
        assertEquals(0, actualListing.getAfter().size());
        assertEquals(2, actualListing.getUpdates().size());

        assertEquals(
                "Like:50f832ae61981432c9000034#536af8633ff873c6f000000c.51afb3bb0046849653000002",
                actualFirstElement.getId());
        assertEquals(1406238917.0174108, actualFirstElement.getCreatedAt());
        assertEquals("Jevon Wild likes your wine.", actualFirstElement.getText());
        assertEquals("delectable://capture?capture_id=536af8633ff873c6f000000c",
                actualFirstElement.getSelectionLink().getUrl());
        assertEquals(null, actualFirstElement.getTextLinks());
        assertEquals("delectable://capture?capture_id=536af8633ff873c6f000000c",
                actualFirstElement.getRightImageLink().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotosStaging/dima-vartanian-1399519330-22368b249605.jpg",
                actualFirstElement.getRightImageLink().getPhoto().getUrl());
        assertEquals("delectable://account?account_id=50f832ae61981432c9000034",
                actualFirstElement.getLeftImageLink().getUrl());
        assertEquals("https://s3.amazonaws.com/delectableStockPhotos/no_photo.png",
                actualFirstElement.getLeftImageLink().getPhoto().getUrl());
        assertEquals("7CcErdSakjr4nQ", actualFirstElement.getETag());
        assertEquals(expectedContext, actualFirstElement.getContext());
    }
}
