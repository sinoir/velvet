package com.delectable.mobile.tests;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.BaseListingElementDeserializer;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.DeleteHash;
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
    private static final Type TYPE
            = new TypeToken<ListingResponse<BaseListingElement, DeleteHash>>() {
    }.getType();

    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //custom deserializer for captures_and_pending_captures endpoint because a mixed array returns
        gsonBuilder.registerTypeAdapter(BaseListingElement.class,
                new BaseListingElementDeserializer());
        mGson = gsonBuilder.create();
    }

    private static void assertCountsEquals(ArrayList<BaseListingElement> displayData,
            int expectedPendingCount, int expectedCaptureCount,
            int expectedTotal) {
        int actualPendingCapturesCount = 0;
        int actualCaptureDetailsCount = 0;

        for (BaseListingElement item : displayData) {
            if (item instanceof PendingCapture) {
                actualPendingCapturesCount++;
            }

            if (item instanceof CaptureDetails) {
                actualCaptureDetailsCount++;
            }
        }

        assertEquals(expectedPendingCount, actualPendingCapturesCount);
        assertEquals(expectedCaptureCount, actualCaptureDetailsCount);
        assertEquals(expectedTotal, displayData.size());
    }

    public void testParseCapturesAndPendingCapturesListing() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_captures_and_pending_captures_details_ctx_stage_1_raw_fetch);

        ListingResponse<BaseListingElement, String> response = mGson
                .fromJson(json.toString(), TYPE);
        Listing<BaseListingElement, String> actualListing = response.getPayload();

        assertCountsEquals(actualListing.getUpdates(), 2, 8, 10);
    }

    public void testCapturesAndPendingCapturesListingWithAfterAndDelete() throws JSONException {
        JSONObject json1 = loadJsonObjectFromResource(
                R.raw.test_accounts_captures_and_pending_captures_details_ctx_stage_1_raw_fetch);
        JSONObject json2WithAfter = loadJsonObjectFromResource(
                R.raw.test_accounts_captures_and_pending_captures_details_ctx_stage_2_with_after);
        JSONObject json3WithDelete = loadJsonObjectFromResource(
                R.raw.test_accounts_captures_and_pending_captures_details_ctx_stage_3_with_delete);

        ListingResponse<BaseListingElement, DeleteHash> response1, response2WithAfter,
                response3WithDelete;
        response1 = mGson.fromJson(json1.toString(), TYPE);
        response2WithAfter = mGson.fromJson(json2WithAfter.toString(), TYPE);
        response3WithDelete = mGson.fromJson(json3WithDelete.toString(), TYPE);

        Listing<BaseListingElement, DeleteHash> listing1, listing2WithAfter, listing3WithDelete;

        listing1 = response1.getPayload();
        listing2WithAfter = response2WithAfter.getPayload();
        listing3WithDelete = response3WithDelete.getPayload();

        ArrayList<BaseListingElement> displayData = listing1.getUpdates();

        assertCountsEquals(displayData, 2, 8, 10);

        listing2WithAfter.combineInto(displayData, response2WithAfter.isInvalidate());

        assertCountsEquals(displayData, 3, 8, 11);

        listing3WithDelete.combineInto(displayData, response3WithDelete.isInvalidate());

        assertCountsEquals(displayData, 2, 8, 10);

    }

    /**
     * Testing for when a pending capture becomes a capture. The same id will appear in the both the
     * deletes array and the updates array.
     */
    public void testCapturesAndPendingCapturesListingWithConvertedCapture() throws JSONException {
        JSONObject json1 = loadJsonObjectFromResource(
                R.raw.test_pending_capture_to_capture_details_ctx_stage_1_raw_fetch);
        JSONObject json2WithConvertedCapture = loadJsonObjectFromResource(
                R.raw.test_pending_capture_to_capture_details_ctx_stage_2_capture_converted);

        ListingResponse<BaseListingElement, DeleteHash> response1, response2WithConvertedCapture;
        response1 = mGson.fromJson(json1.toString(), TYPE);
        response2WithConvertedCapture = mGson.fromJson(json2WithConvertedCapture.toString(), TYPE);

        Listing<BaseListingElement, DeleteHash> listing1, listing2WithConvertedCapture;
        listing1 = response1.getPayload();
        listing2WithConvertedCapture = response2WithConvertedCapture.getPayload();
        String toDeleteId = listing2WithConvertedCapture.getDeletes().get(0).toString();

        ArrayList<BaseListingElement> displayData = listing1.getUpdates();

        assertCountsEquals(displayData, 4, 6, 10);

        int convertingCapturePosition = -1;
        for (int i = 0; i < displayData.size(); i++) {
            BaseListingElement item = displayData.get(i);
            if (item.getId().equals(toDeleteId)) {
                convertingCapturePosition = i;
                break;
            }
        }
        BaseListingElement pendingCapture = displayData.get(convertingCapturePosition);
        assertTrue(pendingCapture instanceof PendingCapture);


        listing2WithConvertedCapture
                .combineInto(displayData, response2WithConvertedCapture.isInvalidate());

        assertCountsEquals(displayData, 3, 7, 10);

        BaseListingElement convertedCapture = displayData.get(convertingCapturePosition);
        assertEquals(convertedCapture.getId(), toDeleteId);
        assertTrue(convertedCapture instanceof CaptureDetails);
    }

    public void testGetFromScratchListing() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_captures_and_pending_captures_details_ctx);
        ListingResponse<BaseListingElement, String> response = mGson
                .fromJson(json.toString(), TYPE);
        Listing<BaseListingElement, String> listing = response.getPayload();

        ArrayList<BaseListingElement> expectedCaptures = listing.getUpdates();

        //simulating initial empty array
        ArrayList<BaseListingElement> actualCaptures = new ArrayList<BaseListingElement>();
        listing.combineInto(actualCaptures, response.isInvalidate());

        assertEquals(expectedCaptures, actualCaptures);
    }

}
