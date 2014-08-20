package com.delectable.mobile.tests;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.api.captures.CaptureDetailsResponse;
import com.delectable.mobile.model.api.captures.CaptureFeedResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class CaptureTest extends BaseInstrumentationTestCase {

    private Type mCaptureResponseType = new TypeToken<CaptureDetailsResponse>() {
    }.getType();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseCaptureDetailsCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_details_ctx);
        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails actualCapture = responseObject.payload.capture;

        assertEquals("535be3177534906c8b0007d8", actualCapture.getId());
        assertEquals(1398530839.6019998, actualCapture.getCreatedAt());
        assertEquals(false, actualCapture.getPrivate().booleanValue());

        assertEquals(-1, actualCapture.getRatings().get("531626c71d2b11c1a400004e").intValue());
        assertEquals(19, actualCapture.getRatings().get("52069ff93166785b5d003576").intValue());

        assertEquals("details", actualCapture.getContext());
        assertEquals("QwG22R7comW3SA", actualCapture.getETag());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646.jpg",
                actualCapture.getPhoto().getUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_nano.jpg",
                actualCapture.getPhoto().getNanoUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_micro.jpg",
                actualCapture.getPhoto().getMicroUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_thumb.jpg",
                actualCapture.getPhoto().getThumbUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_250x250.jpg",
                actualCapture.getPhoto().get250Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_450x450.jpg",
                actualCapture.getPhoto().get450Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_medium.jpg",
                actualCapture.getPhoto().getMediumUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_blur.jpg",
                actualCapture.getPhoto().getBlurUrl());
        assertEquals("http://del.ec/1Tk5A", actualCapture.getShortShareUrl());
        assertEquals("Hard black berry. Simple acid. $25", actualCapture.getTweet());

        assertEquals("535be3177534906c8b0007e0", actualCapture.getComments().get(0).getId());
        assertEquals(1398530839.6009998, actualCapture.getComments().get(0).getCreatedAt());
        assertEquals("Hard black berry. Simple acid. $25",
                actualCapture.getComments().get(0).getComment());
        assertEquals("52069ff93166785b5d003576", actualCapture.getComments().get(0).getAccountId());
        assertEquals("details", actualCapture.getContext());
        assertEquals("QwG22R7comW3SA", actualCapture.getETag());

        assertEquals("5317fef541dbc30b0000005c", actualCapture.getWineProfile().getId());
        assertEquals("2983852", actualCapture.getWineProfile().getRegionId());
        assertEquals("2012", actualCapture.getWineProfile().getVintage());
        assertEquals("Napa Cellars", actualCapture.getWineProfile().getProducerName());
        assertEquals("Napa Valley Cabernet Sauvignon", actualCapture.getWineProfile().getName());
        assertEquals("5305bb6a8953f6d739026e2a", actualCapture.getWineProfile().getBaseWineId());
        assertEquals(null, actualCapture.getWineProfile().getPrice());
        assertEquals("unconfirmed", actualCapture.getWineProfile().getPriceStatus());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258.jpg",
                actualCapture.getWineProfile().getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_nano.jpg",
                actualCapture.getWineProfile().getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_micro.jpg",
                actualCapture.getWineProfile().getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_thumb.jpg",
                actualCapture.getWineProfile().getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_250x250.jpg",
                actualCapture.getWineProfile().getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_450x450.jpg",
                actualCapture.getWineProfile().getPhoto().get450Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_medium.jpg",
                actualCapture.getWineProfile().getPhoto().getMediumUrl());
        assertEquals("", actualCapture.getWineProfile().getDescription());
        assertEquals("minimal", actualCapture.getWineProfile().getContext());
        assertEquals("AzUzYP2ykkHuxQ", actualCapture.getWineProfile().getETag());
        assertEquals(null, actualCapture.getWineProfile().getPriceText());

        assertEquals(null, actualCapture.getBaseWine());

        assertEquals("52069ff93166785b5d003576", actualCapture.getCapturerParticipant().getId());
        assertEquals("Austin", actualCapture.getCapturerParticipant().getFname());
        assertEquals("Beeman", actualCapture.getCapturerParticipant().getLname());
        assertEquals("http://graph.facebook.com/542133246/picture?width=200",
                actualCapture.getCapturerParticipant().getPhoto().getUrl());
        assertEquals(false, actualCapture.getCapturerParticipant().getInfluencer().booleanValue());
        assertEquals("", actualCapture.getCapturerParticipant().getInfluencerTitles().get(0));
        assertEquals("uk12p-STHmlsbA", actualCapture.getCapturerParticipant().getETag());

        assertEquals("52069ff93166785b5d003576",
                actualCapture.getCommentingParticipants().get(0).getId());
        assertEquals("Austin", actualCapture.getCommentingParticipants().get(0).getFname());
        assertEquals("Beeman", actualCapture.getCommentingParticipants().get(0).getLname());
        assertEquals("http://graph.facebook.com/542133246/picture?width=200",
                actualCapture.getCommentingParticipants().get(0).getPhoto().getUrl());
        assertEquals(false,
                actualCapture.getCommentingParticipants().get(0).getInfluencer().booleanValue());
        assertEquals("",
                actualCapture.getCommentingParticipants().get(0).getInfluencerTitles().get(0));
        assertEquals("uk12p-STHmlsbA", actualCapture.getCommentingParticipants().get(0).getETag());

        assertEquals(0, actualCapture.getLikingParticipants().size());
        assertEquals(0, actualCapture.getFacebookParticipants().size());
        assertEquals(0, actualCapture.getContactParticipants().size());

        assertEquals(1, actualCapture.getRegisteredParticipants().size());
        assertEquals("531626c71d2b11c1a400004e",
                actualCapture.getRegisteredParticipants().get(0).getId());
        assertEquals("Jim", actualCapture.getRegisteredParticipants().get(0).getFname());
        assertEquals("Krusinski", actualCapture.getRegisteredParticipants().get(0).getLname());
        assertEquals("https://s3.amazonaws.com/delectableStockPhotos/no_photo.png",
                actualCapture.getRegisteredParticipants().get(0).getPhoto().getUrl());
        assertEquals(false,
                actualCapture.getRegisteredParticipants().get(0).getInfluencer().booleanValue());
        assertEquals("",
                actualCapture.getRegisteredParticipants().get(0).getInfluencerTitles().get(0));
        assertEquals("tJIa96k_mnebug", actualCapture.getRegisteredParticipants().get(0).getETag());
    }

    public void testParseCaptureMinimalCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_minimal_ctx);

        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails actualCapture = responseObject.payload.capture;

        assertEquals("535be3177534906c8b0007d8", actualCapture.getId());
        assertEquals(1398530839.6019998, actualCapture.getCreatedAt());
        assertEquals(false, actualCapture.getPrivate().booleanValue());

        assertEquals(-1, actualCapture.getRatings().get("531626c71d2b11c1a400004e").intValue());
        assertEquals(19, actualCapture.getRatings().get("52069ff93166785b5d003576").intValue());

        assertEquals("minimal", actualCapture.getContext());
        assertEquals("MUrnOhq7VUuJKw", actualCapture.getETag());

        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646.jpg",
                actualCapture.getPhoto().getUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_nano.jpg",
                actualCapture.getPhoto().getNanoUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_micro.jpg",
                actualCapture.getPhoto().getMicroUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_thumb.jpg",
                actualCapture.getPhoto().getThumbUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_250x250.jpg",
                actualCapture.getPhoto().get250Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_450x450.jpg",
                actualCapture.getPhoto().get450Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_medium.jpg",
                actualCapture.getPhoto().getMediumUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/austin-beeman-1398530815-29e8c8f77646_blur.jpg",
                actualCapture.getPhoto().getBlurUrl());
        assertEquals("http://del.ec/1Tk5A", actualCapture.getShortShareUrl());
        assertEquals("Hard black berry. Simple acid. $25", actualCapture.getTweet());

        assertEquals("5317fef541dbc30b0000005c", actualCapture.getWineProfile().getId());
        assertEquals("2983852", actualCapture.getWineProfile().getRegionId());
        assertEquals("2012", actualCapture.getWineProfile().getVintage());
        assertEquals("Napa Cellars", actualCapture.getWineProfile().getProducerName());
        assertEquals("Napa Valley Cabernet Sauvignon", actualCapture.getWineProfile().getName());
        assertEquals("5305bb6a8953f6d739026e2a", actualCapture.getWineProfile().getBaseWineId());
        assertEquals(null, actualCapture.getWineProfile().getPrice());
        assertEquals("unconfirmed", actualCapture.getWineProfile().getPriceStatus());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258.jpg",
                actualCapture.getWineProfile().getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_nano.jpg",
                actualCapture.getWineProfile().getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_micro.jpg",
                actualCapture.getWineProfile().getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_thumb.jpg",
                actualCapture.getWineProfile().getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_250x250.jpg",
                actualCapture.getWineProfile().getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_450x450.jpg",
                actualCapture.getWineProfile().getPhoto().get450Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/brent-suiter-1394080620-bfe1744a2258_medium.jpg",
                actualCapture.getWineProfile().getPhoto().getMediumUrl());
        assertEquals("", actualCapture.getWineProfile().getDescription());
        assertEquals("minimal", actualCapture.getWineProfile().getContext());
        assertEquals("AzUzYP2ykkHuxQ", actualCapture.getWineProfile().getETag());
        assertEquals(null, actualCapture.getWineProfile().getPriceText());

        assertEquals(null, actualCapture.getBaseWine());
    }

    public void testGetRatingPercentForId() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_minimal_ctx);

        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails actualCapture = responseObject.payload.capture;
        float expectedNoRating = -1.0f;
        float expectedGoodRating = 19.0f / 40.0f;
        assertEquals(expectedNoRating,
                actualCapture.getRatingPercentForId("531626c71d2b11c1a400004e"));
        assertEquals(expectedGoodRating, actualCapture.getRatingPercentForId(
                "52069ff93166785b5d003576"));
        assertEquals(expectedNoRating, actualCapture.getRatingPercentForId("abc"));
    }

    public void testGetRatingForId() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_minimal_ctx);

        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails actualCapture = responseObject.payload.capture;
        int expectedNoRating = -1;
        int expectedGoodRating = 19;
        // Test Existing User with No Rating
        assertEquals(expectedNoRating, actualCapture.getRatingForId("531626c71d2b11c1a400004e"));
        // Test Existing User With Rating
        assertEquals(expectedGoodRating,
                actualCapture.getRatingForId("52069ff93166785b5d003576"));
        // Test Non Existing User With No Rating
        assertEquals(expectedNoRating, actualCapture.getRatingForId("abc"));
    }

    public void testUpdateCaptureRating() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_minimal_ctx);

        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails capture = responseObject.payload.capture;

        // Tests if user doesn't have a rating
        String userAccountId = "abc";
        int rating = 34;
        assertFalse(capture.getRatings().containsKey(userAccountId));
        capture.updateRatingForUser(userAccountId, rating);
        assertEquals(rating, capture.getRatings().get(userAccountId).intValue());

        // Tests if existing user likes capture, and then toggles to unlike capture
        userAccountId = "531626c71d2b11c1a400004e";
        rating = 13;
        assertTrue(capture.getRatings().containsKey(userAccountId));
        capture.updateRatingForUser(userAccountId, rating);
        assertEquals(rating, capture.getRatings().get(userAccountId).intValue());
    }

    public void testGetCommentForUserIdFromCaptureWithComments() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_details_ctx);

        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails capture = responseObject.payload.capture;

        CaptureComment actualComment = capture.getCommentsForUserId("52069ff93166785b5d003576")
                .get(0);

        String expectedCommentString = "Hard black berry. Simple acid. $25";
        assertEquals(expectedCommentString, actualComment.getComment());
    }

    public void testGetCommentForUserIdFromCaptureWithCommentsAndBadUserId() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_details_ctx);

        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails capture = responseObject.payload.capture;

        ArrayList<CaptureComment> actualComments = capture.getCommentsForUserId("unknown?");

        assertEquals(0, actualComments.size());
    }

    public void testGetCommentForUserIdFromCaptureWithNoComments() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_details_ctx);

        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails capture = responseObject.payload.capture;

        // Clear comments with valud array list
        capture.getComments().clear();
        ArrayList<CaptureComment> actualComments = capture
                .getCommentsForUserId("52069ff93166785b5d003576");
        assertEquals(0, actualComments.size());

        // Comments is Null
        capture.setComments(null);
        actualComments = capture.getCommentsForUserId("52069ff93166785b5d003576");
        assertEquals(0, actualComments.size());
    }

    public void testGetCreationDate() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_details_ctx);

        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails capture = responseObject.payload.capture;

        Date expectedDate = new Date(1398530839601l);
        Date actualDate = capture.getCreatedAtDate();
        assertEquals(expectedDate, actualDate);
    }

    public void testGetLikesCount() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_details_ctx);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> captureListing = feedResponseObject.payload;

        CaptureDetails capture = captureListing.getUpdates().get(3);
        assertEquals(3, capture.getLikesCount());
    }

    public void testGetLikesCounWithNullLikingParticipants() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_minimal_ctx);
        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails capture = responseObject.payload.capture;
        assertEquals(0, capture.getLikesCount());
    }

    public void testDoesUserLikeCaptureWithLikingParticipants() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_details_ctx);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> captureListing = feedResponseObject.payload;

        CaptureDetails capture = captureListing.getUpdates().get(3);
        String userAccountId = "abc";
        assertFalse(capture.doesUserLikeCapture(userAccountId));

        userAccountId = "517809692dbf68273f000711";
        assertTrue(capture.doesUserLikeCapture(userAccountId));
    }

    public void testToggleUserLikesCapture() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_details_ctx);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(json.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> captureListing = feedResponseObject.payload;

        CaptureDetails capture = captureListing.getUpdates().get(3);
        // Tests if user doesn't like capture, and toggling makes user like the capture
        String userAccountId = "abc";
        assertFalse(capture.doesUserLikeCapture(userAccountId));
        capture.toggleUserLikesCapture(userAccountId);
        assertTrue(capture.doesUserLikeCapture(userAccountId));

        // Tests if existing user likes capture, and then toggles to unlike capture
        userAccountId = "517809692dbf68273f000711";
        assertTrue(capture.doesUserLikeCapture(userAccountId));
        capture.toggleUserLikesCapture(userAccountId);
        assertFalse(capture.doesUserLikeCapture(userAccountId));
    }

    public void testDoesUserLikeCaptureWithNullLikingParticipants() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_capture_minimal_ctx);
        CaptureDetailsResponse responseObject = mGson
                .fromJson(json.toString(), CaptureDetailsResponse.class);
        CaptureDetails capture = responseObject.payload.capture;
        assertNull(capture.getLikingParticipants());
        String userAccountId = "";
        assertFalse(capture.doesUserLikeCapture(userAccountId));
    }

    public void testCaptureCreatedAtDescendingComparator() {
        CaptureDetails firstCap = new CaptureDetails();
        CaptureDetails secondCap = new CaptureDetails();
        CaptureDetails thirdCap = new CaptureDetails();
        firstCap.setCreatedAt(3.0);
        secondCap.setCreatedAt(1.0);
        thirdCap.setCreatedAt(4.0);

        ArrayList<CaptureDetails> actualList = new ArrayList<CaptureDetails>();
        actualList.add(firstCap);
        actualList.add(secondCap);
        actualList.add(thirdCap);

        ArrayList<CaptureDetails> expectedSortedList = new ArrayList<CaptureDetails>();
        expectedSortedList.add(thirdCap);
        expectedSortedList.add(firstCap);
        expectedSortedList.add(secondCap);

        Collections.sort(actualList, CaptureDetails.CreatedAtDescendingComparator);

        assertEquals(expectedSortedList, actualList);
    }

    public void testUpdateCaptureDetails() throws JSONException {
        // Get "Old" capture
        JSONObject jsonDetails = loadJsonObjectFromResource(R.raw.test_capture_details_ctx);
        CaptureDetailsResponse responseObject = mGson
                .fromJson(jsonDetails.toString(), CaptureDetailsResponse.class);
        CaptureDetails capture = responseObject.payload.capture;

        // Get "Updated" capture
        JSONObject jsonFeed = loadJsonObjectFromResource(
                R.raw.test_accounts_follower_feed_details_ctx);
        CaptureFeedResponse feedResponseObject = mGson
                .fromJson(jsonFeed.toString(), CaptureFeedResponse.class);
        ListingResponse<CaptureDetails> captureListing = feedResponseObject.payload;

        // this is just a test, ideally the updated capture will have the same ID
        CaptureDetails updatedCapture = captureListing.getUpdates().get(3);
        updatedCapture.setId(capture.getId());

        capture.updateWithNewCapture(updatedCapture);

        assertEquals(updatedCapture.getShortShareUrl(), capture.getShortShareUrl());
        assertEquals(updatedCapture.getTweet(), capture.getTweet());
        assertEquals(updatedCapture.getRatings(), capture.getRatings());
        assertEquals(updatedCapture.getPhoto(), capture.getPhoto());
        assertEquals(updatedCapture.getBaseWine(), capture.getBaseWine());
        assertEquals(updatedCapture.getWineProfile(), capture.getWineProfile());
        assertEquals(updatedCapture.getTranscriptionErrorMessage(),
                capture.getTranscriptionErrorMessage());
        assertEquals(updatedCapture.getLocationName(), capture.getLocationName());
        assertEquals(updatedCapture.getLikingParticipants(), capture.getLikingParticipants());
        assertEquals(updatedCapture.getCommentingParticipants(),
                capture.getCommentingParticipants());
        assertEquals(updatedCapture.getTaggeeParticipants(), capture.getTaggeeParticipants());
        assertEquals(updatedCapture.getComments(), capture.getComments());
        assertEquals(updatedCapture.getETag(), capture.getETag());
    }
}
