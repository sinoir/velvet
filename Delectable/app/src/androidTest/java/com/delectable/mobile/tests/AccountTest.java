package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureDetailsListing;
import com.delectable.mobile.api.models.CaptureSummary;
import com.delectable.mobile.api.requests.AccountsContextRequest;
import com.delectable.mobile.api.requests.AccountsFollowerFeedRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountTest extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseAccountsPrivateCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_private_ctx);
        String expectedContext = "private";
        AccountsContextRequest request = new AccountsContextRequest(
                AccountsContextRequest.CONTEXT_PRIVATE);
        assertEquals(expectedContext, request.getContext());

        Account actualAccount = (Account) request.buildResopnseFromJson(json);
        assertEquals("537e2f09753490201d00084e", actualAccount.getId());
        assertEquals("Adam", actualAccount.getFname());
        assertEquals("Bednarek", actualAccount.getLname());
        assertEquals(false, actualAccount.getInfluencer().booleanValue());
        assertEquals("", actualAccount.getInfluencerTitles().get(0));
        assertEquals("", actualAccount.getBio());
        assertEquals(0, actualAccount.getFollowerCount().intValue());
        assertEquals(2, actualAccount.getFollowingCount().intValue());
        assertEquals(0, actualAccount.getCaptureCount().intValue());
        assertEquals(0, actualAccount.getRegionCount().intValue());
        assertEquals(0, actualAccount.getWishlistCount().intValue());
        assertEquals("", actualAccount.getUrl());
        assertEquals("https://s3.amazonaws.com/delectableStockPhotos/no_photo.png",
                actualAccount.getPhoto().getUrl());

        assertEquals(false, actualAccount.getAccountConfig().getPassiveOgSharing().booleanValue());
        assertEquals(false,
                actualAccount.getAccountConfig().getPassiveVintankSharing().booleanValue());
        assertEquals(true,
                actualAccount.getAccountConfig().getPnCaptureTranscribed().booleanValue());
        assertEquals(true, actualAccount.getAccountConfig().getPnCommentOnOwnWine().booleanValue());
        assertEquals(true, actualAccount.getAccountConfig().getPnCommentResponse().booleanValue());
        assertEquals(true, actualAccount.getAccountConfig().getPnExperiment().booleanValue());
        assertEquals(true, actualAccount.getAccountConfig().getPnFriendJoined().booleanValue());
        assertEquals(true, actualAccount.getAccountConfig().getPnLikeOnOwnWine().booleanValue());
        assertEquals(true, actualAccount.getAccountConfig().getPnNewFollower().booleanValue());
        assertEquals(true,
                actualAccount.getAccountConfig().getPnPurchaseOfferMade().booleanValue());
        assertEquals(true, actualAccount.getAccountConfig().getPnTagged().booleanValue());
        assertEquals(0, actualAccount.getAccountConfig().getTaggingTest().intValue());

        assertEquals("email", actualAccount.getIdentifiers().get(0).getType());
        assertEquals("537e2f09753490201d000851", actualAccount.getIdentifiers().get(0).getId());
        assertEquals("adam@ad60.com", actualAccount.getIdentifiers().get(0).getString());
        assertEquals(false, actualAccount.getIdentifiers().get(0).getVerified().booleanValue());
        assertEquals(true, actualAccount.getIdentifiers().get(0).getPrimary().booleanValue());

        assertEquals("CALIFORNIA", actualAccount.getSourcingState());
        assertEquals(0, actualAccount.getShippingAddresses().size());
        assertEquals(0, actualAccount.getPaymentMethods().size());
        assertEquals("adam@ad60.com", actualAccount.getEmail());

        assertEquals(0, actualAccount.getActivityFeedTsLast());
        assertEquals(true, actualAccount.getFtueCompleted());
        assertEquals(true, actualAccount.getLocalNotifs().getSendLnOne().booleanValue());
        assertEquals(true, actualAccount.getLocalNotifs().getSendLnTwo().booleanValue());
        assertEquals(false, actualAccount.getLocalNotifs().getSendLnThree().booleanValue());
        assertEquals(false, actualAccount.getLocalNotifs().getSendLnFour().booleanValue());
        assertEquals(false, actualAccount.getLocalNotifs().getSendLnFive().booleanValue());
    }

    public void testParseAccountsMyProfileCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_my_profile_ctx);
        String expectedContext = "profile";
        AccountsContextRequest request = new AccountsContextRequest(
                AccountsContextRequest.CONTEXT_PROFILE);
        assertEquals(expectedContext, request.getContext());

        Account actualAccount = (Account) request.buildResopnseFromJson(json);

        assertEquals("537e2f09753490201d00084e", actualAccount.getId());
        assertEquals("Adam", actualAccount.getFname());
        assertEquals("Bednarek", actualAccount.getLname());
        assertEquals(false, actualAccount.getInfluencer().booleanValue());
        assertEquals("", actualAccount.getInfluencerTitles().get(0));
        assertEquals("", actualAccount.getBio());
        assertEquals(0, actualAccount.getFollowerCount().intValue());
        assertEquals(2, actualAccount.getFollowingCount().intValue());
        assertEquals(0, actualAccount.getCaptureCount().intValue());
        assertEquals(0, actualAccount.getRegionCount().intValue());
        assertEquals(0, actualAccount.getWishlistCount().intValue());
        assertEquals("", actualAccount.getUrl());
        assertEquals("https://s3.amazonaws.com/delectableStockPhotos/no_photo.png",
                actualAccount.getPhoto().getUrl());

        assertEquals(expectedContext, actualAccount.getContext());
        assertEquals("jNHflR9U9xRQjA", actualAccount.getETag());
        assertEquals(-1, actualAccount.getCurrentUserRelationship().intValue());
        assertEquals(0, actualAccount.getCaptureSummaries().size());
    }

    public void testParseAccountsOtherProfileCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_other_profile_ctx);
        String expectedContext = "profile";
        AccountsContextRequest request = new AccountsContextRequest(
                AccountsContextRequest.CONTEXT_PROFILE);
        assertEquals(expectedContext, request.getContext());

        Account actualAccount = (Account) request.buildResopnseFromJson(json);

        assertEquals("51d24187b4db0164af000206", actualAccount.getId());
        assertEquals("James", actualAccount.getFname());
        assertEquals("Wooldridge", actualAccount.getLname());
        assertEquals(false, actualAccount.getInfluencer().booleanValue());
        assertEquals("", actualAccount.getInfluencerTitles().get(0));
        assertEquals("", actualAccount.getBio());
        assertEquals(2, actualAccount.getFollowerCount().intValue());
        assertEquals(6, actualAccount.getFollowingCount().intValue());
        assertEquals(39, actualAccount.getCaptureCount().intValue());
        assertEquals(30, actualAccount.getRegionCount().intValue());
        assertEquals(0, actualAccount.getWishlistCount().intValue());
        assertEquals("", actualAccount.getUrl());
        assertEquals("http://graph.facebook.com/1580152674/picture?width=200",
                actualAccount.getPhoto().getUrl());
        assertEquals("profile", actualAccount.getContext());
        assertEquals("H_Noq-ksM8U4Hw", actualAccount.getETag());
        assertEquals(0, actualAccount.getCurrentUserRelationship().intValue());

        assertEquals(1, actualAccount.getCaptureSummaries().size());
        CaptureSummary firstCapSummary = actualAccount.getCaptureSummaries().get(0);
        assertEquals("Most Recent", actualAccount.getCaptureSummaries().get(0).getTitle());
        assertEquals("See more Most Recent",
                actualAccount.getCaptureSummaries().get(0).getMoreTitle());
        assertEquals("most_recent", actualAccount.getCaptureSummaries().get(0).getType());

        assertEquals(5, firstCapSummary.getCaptures().size());
        CaptureDetails firstCapDetail = firstCapSummary.getCaptures().get(0);
        assertEquals("536d6de9753490705c00011f", firstCapDetail.getId());
        assertEquals(1399680489.386, firstCapDetail.getCreatedAt());
        assertEquals(false, firstCapDetail.getPrivate().booleanValue());
        assertEquals(28, firstCapDetail.getRatings().get("51d24187b4db0164af000206").intValue());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/james-wooldridge-1399680465-1eb01e78bc36.jpg",
                firstCapDetail.getPhoto().getUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/james-wooldridge-1399680465-1eb01e78bc36_nano.jpg",
                firstCapDetail.getPhoto().getNanoUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/james-wooldridge-1399680465-1eb01e78bc36_micro.jpg",
                firstCapDetail.getPhoto().getMicroUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/james-wooldridge-1399680465-1eb01e78bc36_thumb.jpg",
                firstCapDetail.getPhoto().getThumbUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/james-wooldridge-1399680465-1eb01e78bc36_250x250.jpg",
                firstCapDetail.getPhoto().get250Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/james-wooldridge-1399680465-1eb01e78bc36_450x450.jpg",
                firstCapDetail.getPhoto().get450Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/james-wooldridge-1399680465-1eb01e78bc36_medium.jpg",
                firstCapDetail.getPhoto().getMediumUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/james-wooldridge-1399680465-1eb01e78bc36_blur.jpg",
                firstCapDetail.getPhoto().getBlurUrl());

        assertEquals("http://del.ec/L3A7A", firstCapDetail.getShortShareUrl());
        assertEquals("\"I'd drink this again\". Said my wife. ", firstCapDetail.getTweet());

        assertEquals("533ef42edeafe609004959fc", firstCapDetail.getWineProfile().getId());
        assertEquals("1760015", firstCapDetail.getWineProfile().getRegionId());
        assertEquals("2012", firstCapDetail.getWineProfile().getVintage());
        assertEquals("Erath", firstCapDetail.getWineProfile().getProducerName());
        assertEquals("Oregon Pinot Noir", firstCapDetail.getWineProfile().getName());
        assertEquals("5305bb2f8953f6d739024224", firstCapDetail.getWineProfile().getBaseWineId());
        assertEquals(14.92, firstCapDetail.getWineProfile().getPrice());
        assertEquals("confirmed", firstCapDetail.getWineProfile().getPriceStatus());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/riccardo-febbrari-1396632748-47c958d76deb.jpg",
                firstCapDetail.getWineProfile().getPhoto().getUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/riccardo-febbrari-1396632748-47c958d76deb_nano.jpg",
                firstCapDetail.getWineProfile().getPhoto().getNanoUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/riccardo-febbrari-1396632748-47c958d76deb_micro.jpg",
                firstCapDetail.getWineProfile().getPhoto().getMicroUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/riccardo-febbrari-1396632748-47c958d76deb_thumb.jpg",
                firstCapDetail.getWineProfile().getPhoto().getThumbUrl());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/riccardo-febbrari-1396632748-47c958d76deb_250x250.jpg",
                firstCapDetail.getWineProfile().getPhoto().get250Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/riccardo-febbrari-1396632748-47c958d76deb_450x450.jpg",
                firstCapDetail.getWineProfile().getPhoto().get450Url());
        assertEquals(
                "https://d2mvsg0ph94s7h.cloudfront.net/riccardo-febbrari-1396632748-47c958d76deb_medium.jpg",
                firstCapDetail.getWineProfile().getPhoto().getMediumUrl());
        assertEquals("", firstCapDetail.getWineProfile().getDescription());
        assertEquals("Buy it - $14", firstCapDetail.getWineProfile().getPriceText());
        assertEquals("minimal", firstCapDetail.getWineProfile().getContext());
        assertEquals("XSHVBNGUt-a6-g", firstCapDetail.getWineProfile().getETag());

        assertEquals(null, firstCapDetail.getBaseWine());
        assertEquals("minimal", firstCapDetail.getContext());
        assertEquals("0s2exXRTo5MDZA", firstCapDetail.getETag());
    }

    public void testParseAccountFollowerFeedMinCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_min_ctx);
        String expectedContext = "minimal";
        AccountsFollowerFeedRequest request = new AccountsFollowerFeedRequest(
                AccountsFollowerFeedRequest.CONTEXT_MINIMAL);
        assertEquals(expectedContext, request.getContext());

        CaptureDetailsListing actualListing = (CaptureDetailsListing) request
                .buildResopnseFromJson(json);

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
        assertEquals(expectedContext, actualListing.getContext());

        CaptureDetails actualFirstUpdateCapture = actualListing.getUpdates().get(0);

        assertEquals("5386a2077534901de4000004", actualFirstUpdateCapture.getId());
        assertEquals(1401332231.559, actualFirstUpdateCapture.getCreatedAt());
        assertEquals(false, actualFirstUpdateCapture.getPrivate().booleanValue());
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
        assertEquals(-1, actualFirstUpdateCapture.getWineProfile().getPrice().intValue());
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

    public void testParseAccountFollowerFeedDetailsCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_follower_feed_details_ctx);
        String expectedContext = "details";
        AccountsFollowerFeedRequest request = new AccountsFollowerFeedRequest(
                AccountsFollowerFeedRequest.CONTEXT_DETAILS);
        assertEquals(expectedContext, request.getContext());

        CaptureDetailsListing actualListing = (CaptureDetailsListing) request
                .buildResopnseFromJson(json);

        assertEquals(5, actualListing.getUpdates().size());
        CaptureDetails actualCapture = actualListing.getUpdates().get(2);
        assertEquals("538cbc831d2b119c4500008e", actualCapture.getId());
        assertEquals(1401732227.191, actualCapture.getCreatedAt());
        assertEquals(false, actualCapture.getPrivate().booleanValue());
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
        assertEquals(expectedContext, actualCapture.getContext());
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
        assertEquals(true, actualCapture.getCapturerParticipant().getInfluencer().booleanValue());
        assertEquals("Owner/Winemaker Weingut Dönnhoff",
                actualCapture.getCapturerParticipant().getInfluencerTitles().get(0));
        assertEquals("minimal", actualCapture.getCapturerParticipant().getContext());
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
                actualCapture.getLikingParticipants().get(1).getInfluencer().booleanValue());
        assertEquals("Buyer Wine Library",
                actualCapture.getLikingParticipants().get(1).getInfluencerTitles().get(0));
        assertEquals("minimal", actualCapture.getLikingParticipants().get(1).getContext());
        assertEquals("h2jSaWRTOhAVMA", actualCapture.getLikingParticipants().get(1).getETag());

        assertEquals(0, actualCapture.getFacebookParticipants().size());
        assertEquals(0, actualCapture.getContactParticipants().size());
        assertEquals(0, actualCapture.getRegisteredParticipants().size());
    }
}
