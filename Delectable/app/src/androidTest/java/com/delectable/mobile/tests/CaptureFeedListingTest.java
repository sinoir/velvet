package com.delectable.mobile.tests;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.api.endpointmodels.BaseListingWrapperResponse;
import com.delectable.mobile.api.endpointmodels.captures.CaptureFeedResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CaptureFeedListingTest extends BaseInstrumentationTestCase {

    private static final String TAG = CaptureFeedListingTest.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseAccountFollowerFeedMinCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_min_ctx);

        Type type = new TypeToken<BaseListingWrapperResponse<CaptureDetails>>() {
        }.getType();
        BaseListingWrapperResponse<CaptureDetails> feedResponseObject = mGson.fromJson(
                json.toString(), type);
        BaseListingResponse<CaptureDetails> actualListing = feedResponseObject.getPayload();

        assertNull(actualListing.getBoundariesFromBefore());
        assertNull(actualListing.getBoundariesFromAfter());
        assertNull(actualListing.getBoundariesFromSince());

        assertEquals("5358087d1d2b11888d000015", actualListing.getBoundariesToBefore());
        assertEquals("5386a2077534901de4000004", actualListing.getBoundariesToAfter());
        assertEquals("1401392575.8950038", actualListing.getBoundariesToSince());
        assertEquals(0, actualListing.getBefore().size());
        assertEquals(0, actualListing.getAfter().size());
        assertEquals(20, actualListing.getUpdates().size());

        assertEquals(0, actualListing.getDeletes().size());
        assertTrue(actualListing.getMore());

        assertEquals("1401392575.8950038", actualListing.getETag());

        CaptureDetails actualFirstUpdateCapture = actualListing.getUpdates().get(0);

        assertEquals("5386a2077534901de4000004", actualFirstUpdateCapture.getId());
        assertEquals(1401332231.559, actualFirstUpdateCapture.getCreatedAt());
        assertEquals(false, actualFirstUpdateCapture.getPrivate());
        assertEquals(33,
                actualFirstUpdateCapture.getRatings().get("50493e4de498ee00020006e5").intValue());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/michael-madrigale-1401332222-36883f2386ce.jpg",
                actualFirstUpdateCapture.getPhoto().getUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/michael-madrigale-1401332222-36883f2386ce_nano.jpg",
                actualFirstUpdateCapture.getPhoto().getNanoUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/michael-madrigale-1401332222-36883f2386ce_micro.jpg",
                actualFirstUpdateCapture.getPhoto().getMicroUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/michael-madrigale-1401332222-36883f2386ce_thumb.jpg",
                actualFirstUpdateCapture.getPhoto().getThumbUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/michael-madrigale-1401332222-36883f2386ce_250x250.jpg",
                actualFirstUpdateCapture.getPhoto().get250Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/michael-madrigale-1401332222-36883f2386ce_450x450.jpg",
                actualFirstUpdateCapture.getPhoto().get450Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/michael-madrigale-1401332222-36883f2386ce_medium.jpg",
                actualFirstUpdateCapture.getPhoto().getMediumUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/michael-madrigale-1401332222-36883f2386ce_blur.jpg",
                actualFirstUpdateCapture.getPhoto().getBlurUrl());
        assertEquals("http://del.ec/sj8gg2A", actualFirstUpdateCapture.getShortShareUrl());
        assertEquals("Close your eyes and you think it's Puligny. GREAT",
                actualFirstUpdateCapture.getTweet());
        assertEquals("521a25a7f05a32ac00000002",
                actualFirstUpdateCapture.getWineProfile().getId());
        assertEquals("1625419", actualFirstUpdateCapture.getWineProfile().getRegionId());
        assertEquals("--", actualFirstUpdateCapture.getWineProfile().getVintage());
        assertEquals("Edmond Cornu & Fils",
                actualFirstUpdateCapture.getWineProfile().getProducerName());
        assertEquals("Ladoix Vieilles Vignes Pinot Noir",
                actualFirstUpdateCapture.getWineProfile().getName());
        assertEquals("5305bb0fe5c5cb8a3a000a83",
                actualFirstUpdateCapture.getWineProfile().getBaseWineId());
        assertEquals(-1.0, actualFirstUpdateCapture.getWineProfile().getPrice());
        assertEquals("impossible",
                actualFirstUpdateCapture.getWineProfile().getPriceStatus());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/julien-mallus-1377436647-483177d7d3ba.jpg",
                actualFirstUpdateCapture.getWineProfile().getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/julien-mallus-1377436647-483177d7d3ba_micro.jpg",
                actualFirstUpdateCapture.getWineProfile().getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/julien-mallus-1377436647-483177d7d3ba_thumb.jpg",
                actualFirstUpdateCapture.getWineProfile().getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/julien-mallus-1377436647-483177d7d3ba_medium.jpg",
                actualFirstUpdateCapture.getWineProfile().getPhoto().getMediumUrl());
        assertEquals("", actualFirstUpdateCapture.getWineProfile().getDescription());
        assertEquals(null, actualFirstUpdateCapture.getWineProfile().getPriceText());
        assertEquals("minimal", actualFirstUpdateCapture.getWineProfile().getContext());
        assertEquals("f9zrnCF5TUb11w", actualFirstUpdateCapture.getWineProfile().getETag());
        assertEquals(null, actualFirstUpdateCapture.getBaseWine());
        assertEquals("minimal", actualFirstUpdateCapture.getContext());
        assertEquals("FA5VUUsMpE0WcQ", actualFirstUpdateCapture.getETag());
    }

    public void testParseAccountFollowerFeedDetailsCtxWithInvalidate() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_details_ctx);

        Type type = new TypeToken<BaseListingWrapperResponse<CaptureDetails>>() {
        }.getType();
        BaseListingWrapperResponse<CaptureDetails> feedResponseObject = mGson.fromJson(
                json.toString(), type);
        BaseListingResponse<CaptureDetails> actualListing = feedResponseObject.getPayload();

        assertEquals(5, actualListing.getUpdates().size());
        CaptureDetails actualCapture = actualListing.getUpdates().get(2);
        assertEquals("538cbc831d2b119c4500008e", actualCapture.getId());
        assertEquals(1401732227.191, actualCapture.getCreatedAt());
        assertEquals(false, actualCapture.getPrivate());
        assertEquals(-1, actualCapture.getRatings().get("53515c79753490e7b4000031").intValue());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/cornelius-donnhoff-1401732222-494dc9dc0d16.jpg",
                actualCapture.getPhoto().getUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/cornelius-donnhoff-1401732222-494dc9dc0d16_nano.jpg",
                actualCapture.getPhoto().getNanoUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/cornelius-donnhoff-1401732222-494dc9dc0d16_micro.jpg",
                actualCapture.getPhoto().getMicroUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/cornelius-donnhoff-1401732222-494dc9dc0d16_thumb.jpg",
                actualCapture.getPhoto().getThumbUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/cornelius-donnhoff-1401732222-494dc9dc0d16_250x250.jpg",
                actualCapture.getPhoto().get250Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/cornelius-donnhoff-1401732222-494dc9dc0d16_450x450.jpg",
                actualCapture.getPhoto().get450Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/cornelius-donnhoff-1401732222-494dc9dc0d16_medium.jpg",
                actualCapture.getPhoto().getMediumUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/cornelius-donnhoff-1401732222-494dc9dc0d16_blur.jpg",
                actualCapture.getPhoto().getBlurUrl());

        assertEquals("http://del.ec/mssXw6Q", actualCapture.getShortShareUrl());
        assertEquals("Just posted a wine", actualCapture.getTweet());
        assertEquals(0, actualCapture.getCommentingParticipants().size());
        assertEquals("XWJa4ploEboxyw", actualCapture.getETag());

        assertEquals("5159067ae8bd545f210005e3", actualCapture.getWineProfile().getId());
        assertEquals("1603188", actualCapture.getWineProfile().getRegionId());
        assertEquals("2008", actualCapture.getWineProfile().getVintage());
        assertEquals("Joh. Jos. Prüm", actualCapture.getWineProfile().getProducerName());
        assertEquals("Wehlener Sonnenuhr Spätlese Riesling",
                actualCapture.getWineProfile().getName());
        assertEquals("5305bb528953f6d7390261fe", actualCapture.getWineProfile().getBaseWineId());
        assertEquals(null, actualCapture.getWineProfile().getPrice());
        assertEquals("unconfirmed", actualCapture.getWineProfile().getPriceStatus());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/wei-lai-1364788640-1e0fedd3bdea.jpg",
                actualCapture.getWineProfile().getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/wei-lai-1364788640-1e0fedd3bdea_micro.jpg",
                actualCapture.getWineProfile().getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/wei-lai-1364788640-1e0fedd3bdea_thumb.jpg",
                actualCapture.getWineProfile().getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/wei-lai-1364788640-1e0fedd3bdea_medium.jpg",
                actualCapture.getWineProfile().getPhoto().getMediumUrl());
        assertEquals("", actualCapture.getWineProfile().getDescription());
        assertEquals(null, actualCapture.getWineProfile().getPriceText());
        assertEquals("minimal", actualCapture.getWineProfile().getContext());
        assertEquals("K9YunfW_DG_qvw", actualCapture.getWineProfile().getETag());

        assertEquals(null, actualCapture.getBaseWine());

        assertEquals("53515c79753490e7b4000031", actualCapture.getCapturerParticipant().getId());
        assertEquals("Cornelius", actualCapture.getCapturerParticipant().getFname());
        assertEquals("Dönnhoff", actualCapture.getCapturerParticipant().getLname());
        assertEquals("http://graph.facebook.com/1034078462/picture?width=200",
                actualCapture.getCapturerParticipant().getPhoto().getUrl());
        assertEquals(true, actualCapture.getCapturerParticipant().isInfluencer());
        assertEquals("Owner/Winemaker Weingut Dönnhoff",
                actualCapture.getCapturerParticipant().getInfluencerTitles().get(0));
        assertEquals("aedNQFIXBPjvow", actualCapture.getCapturerParticipant().getETag());

        assertEquals(0, actualCapture.getCommentingParticipants().size());
        assertEquals(7, actualCapture.getLikingParticipants().size());

        assertEquals("512640d6bd68767a840009e4",
                actualCapture.getLikingParticipants().get(1).getId());
        assertEquals("Kristen", actualCapture.getLikingParticipants().get(1).getFname());
        assertEquals("Murphy", actualCapture.getLikingParticipants().get(1).getLname());
        assertEquals(
                "https://s3.amazonaws.com/delectable-profile-photos/kristen-murphy-1399171298-2678fc3b42ae.jpg",
                actualCapture.getLikingParticipants().get(1).getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectable-profile-photos/kristen-murphy-1399171298-2678fc3b42ae_nano.jpg",
                actualCapture.getLikingParticipants().get(1).getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectable-profile-photos/kristen-murphy-1399171298-2678fc3b42ae_micro.jpg",
                actualCapture.getLikingParticipants().get(1).getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectable-profile-photos/kristen-murphy-1399171298-2678fc3b42ae_thumb.jpg",
                actualCapture.getLikingParticipants().get(1).getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectable-profile-photos/kristen-murphy-1399171298-2678fc3b42ae_250x250.jpg",
                actualCapture.getLikingParticipants().get(1).getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectable-profile-photos/kristen-murphy-1399171298-2678fc3b42ae_450x450.jpg",
                actualCapture.getLikingParticipants().get(1).getPhoto().get450Url());
        assertEquals(
                "https://s3.amazonaws.com/delectable-profile-photos/kristen-murphy-1399171298-2678fc3b42ae_medium.jpg",
                actualCapture.getLikingParticipants().get(1).getPhoto().getMediumUrl());
        assertEquals(true,
                actualCapture.getLikingParticipants().get(1).isInfluencer());
        assertEquals("Buyer Wine Library",
                actualCapture.getLikingParticipants().get(1).getInfluencerTitles().get(0));
        assertEquals("h2jSaWRTOhAVMA", actualCapture.getLikingParticipants().get(1).getETag());

        assertEquals(0, actualCapture.getFacebookParticipants().size());
        assertEquals(0, actualCapture.getContactParticipants().size());
        assertEquals(0, actualCapture.getRegisteredParticipants().size());
    }

    public void testGetAllCombinedDetails() throws JSONException {
        // Load "First Request" response - As if first resopnse without etag
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_details_befaft_r1);

        Type type = new TypeToken<BaseListingWrapperResponse<CaptureDetails>>() {
        }.getType();
        BaseListingWrapperResponse<CaptureDetails> feedResponseObject = mGson.fromJson(
                json.toString(), type);
        BaseListingResponse<CaptureDetails> captureListing = feedResponseObject.getPayload();

        // Load "Second Request" response, as if getting a request with etag / before / after
        json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_details_befaft_r2);

        feedResponseObject = mGson.fromJson(json.toString(), type);
        BaseListingResponse<CaptureDetails> newListing = feedResponseObject.getPayload();

        //TODO fix
//        newListing.combineWithPreviousListing(captureListing);
//
//        ArrayList<CaptureDetails> expectedCombinedData = new ArrayList<CaptureDetails>();
//        expectedCombinedData.addAll(newListing.getAfter());
//        expectedCombinedData.add(captureListing.getUpdates().get(0));
//        expectedCombinedData.add(captureListing.getUpdates().get(1));
//        expectedCombinedData.add(newListing.getUpdates().get(0));
//        expectedCombinedData.add(newListing.getUpdates().get(1));
//        expectedCombinedData.addAll(newListing.getBefore());
//
//        ArrayList<CaptureDetails> actualCombinedData = newListing.getSortedCombinedData();
//
//        assertEquals(10, actualCombinedData.size());
//        assertEquals(expectedCombinedData, actualCombinedData);
    }

    public void testGetAllCombinedDetailsWithDeletions() throws JSONException {
        // Load "First Request" response - As if first resopnse without etag
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_details_befaft_r1);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> captureListing = feedResponseObject.getPayload();

        // Load "Second Request" response, as if getting a request with etag / before / after with deleted data
        json = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_details_befaft_deletions_r2);
        feedResponseObject = mGson.fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> newListing = feedResponseObject.getPayload();

        newListing.combineWithPreviousListing(captureListing);

        ArrayList<CaptureDetails> expectedCombinedData = new ArrayList<CaptureDetails>();
        expectedCombinedData.addAll(newListing.getAfter());
        expectedCombinedData.add(captureListing.getUpdates().get(0));
        expectedCombinedData.add(captureListing.getUpdates().get(1));
        expectedCombinedData.add(newListing.getUpdates().get(0));
        expectedCombinedData.addAll(newListing.getBefore());

        ArrayList<CaptureDetails> actualCombinedData = newListing.getSortedCombinedData();

        assertEquals(9, actualCombinedData.size());
        assertEquals(expectedCombinedData, actualCombinedData);
    }

    public void testFollowerFeedRequestWithPreivousCapture() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_details_befaft_r1);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> captureListing = feedResponseObject.getPayload();

        String expectedEtag = captureListing.getETag();
        String expectedContext = captureListing.getContext();
        String expectedBefore = captureListing.getBoundariesToBefore();
        String expectedAfter = captureListing.getBoundariesToAfter();

        //TODO implement BaseListingRequest test
//        BaseListingRequest request = new BaseListingRequest();
//        request.setCurrentListing(captureListing);
//        assertEquals(expectedEtag, request.getETag());
//        assertEquals(expectedContext, request.getContext());
//        assertEquals(expectedBefore, request.getBefore());
//        assertEquals(expectedAfter, request.getAfter());
//        assertNull(request.getSuppressBefore());
    }

    public void testFollowerFeedWithNullPayload() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_null_payload);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> actualListing = feedResponseObject.getPayload();
        assertNull(actualListing);
    }


    public void testGetSortedCombinedData() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_details_befaft_r1);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> captureListing = feedResponseObject.getPayload();

        ArrayList<CaptureDetails> expectedCombinedData = captureListing.getUpdates();
        ArrayList<CaptureDetails> actualCombinedData = captureListing.getSortedCombinedData();
        assertEquals(expectedCombinedData, actualCombinedData);
    }

    public void testGetAllCaptireIds() throws JSONException {
        // Load "First Request" response - As if first resopnse without etag
        JSONObject json = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_details_befaft_r1);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> captureListing = feedResponseObject.getPayload();

        // Load "Second Request" response, as if getting a request with etag / before / after with deleted data
        json = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_details_befaft_deletions_r2);
        feedResponseObject = mGson.fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> newListing = feedResponseObject.getPayload();

        newListing.combineWithPreviousListing(captureListing);

        ArrayList<String> actualAllCaptureIds = new ArrayList<String>();

        actualAllCaptureIds.add("5396951b753490a7cd000888");
        actualAllCaptureIds.add("5396917e753490bbed0002ff");
        actualAllCaptureIds.add("539911291d2b1100d30001e4");
        actualAllCaptureIds.add("539535d4753490713f000067");
        actualAllCaptureIds.add("5397912a753490bc6500002b");
        actualAllCaptureIds.add("53968f597534901ccf001253");
        actualAllCaptureIds.add("53993eb5753490f5b3000989");
        actualAllCaptureIds.add("539910ef753490b02e00002a");
        actualAllCaptureIds.add("53993dbd753490bab00000a4");

        assertEquals(actualAllCaptureIds, newListing.getAllIds());
    }
}
