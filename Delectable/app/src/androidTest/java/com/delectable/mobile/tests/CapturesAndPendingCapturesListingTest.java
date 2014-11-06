package com.delectable.mobile.tests;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.BaseListingElementDeserializer;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.PendingCapture;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CapturesAndPendingCapturesListingTest extends BaseInstrumentationTestCase {

    private static final String TAG = CapturesAndPendingCapturesListingTest.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //for gson deserialization
    private static final Type TYPE = new TypeToken<ListingResponse<BaseListingElement, String>>() {
    }.getType();

    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //custom deserializer for captures_and_pending_captures endpoint because a mixed array returns
        gsonBuilder.registerTypeAdapter(BaseListingElement.class, new BaseListingElementDeserializer());
        mGson = gsonBuilder.create();
    }

    public void testParseCapturesAndPendingCapturesListing() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_captures_and_pending_captures_details_ctx);

        ListingResponse<BaseListingElement, String> response = mGson.fromJson(json.toString(), TYPE);
        Listing<BaseListingElement, String> actualListing = response.getPayload();

        int pendingCapturesCount = 0;
        int captureDetailsCount = 0;

        for (BaseListingElement item : actualListing.getUpdates()) {
            if (item instanceof PendingCapture) {
                pendingCapturesCount++;
            }

            if (item instanceof CaptureDetails) {
                captureDetailsCount++;
            }
        }

        assertEquals(2, pendingCapturesCount);
        assertEquals(8, captureDetailsCount);

        assertEquals(10, actualListing.getUpdates().size());

    }

    //TODO test all combining with updates/deletes/before/after
    //the captures_and_pending_captures endpoint does something different where items can show up
    //updates as well as deletes when a pending capture is converted to a capture. we need to keep track of
    //items we remove from the array and their original positions in the array, and then do a check against those removed values when we look
    //through our updates array to see if it was in both arrays. If they were in both arrays then we need to insert that update back
    //into it's original position in the list. the ids for pending capture and it's corresponding capture are
    //guaranteed to be the same
    public void testGetAllCombinedCaptures() throws JSONException {

    }

    public void testGetAllCombinedCapturesWithDeletions() throws JSONException {

    }


    public void testGetFromScratchListing() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_captures_and_pending_captures_details_ctx);
        ListingResponse<BaseListingElement, String> response = mGson.fromJson(json.toString(), TYPE);
        Listing<BaseListingElement, String> listing = response.getPayload();

        ArrayList<BaseListingElement> expectedCaptures = listing.getUpdates();

        //simulating initial empty array
        ArrayList<BaseListingElement> actualCaptures = new ArrayList<BaseListingElement>();
        listing.combineInto(actualCaptures, response.isInvalidate());

        assertEquals(expectedCaptures, actualCaptures);
    }

    public void testCombinedCapturesOrder() throws JSONException {
        //TODO write test
    }
}
