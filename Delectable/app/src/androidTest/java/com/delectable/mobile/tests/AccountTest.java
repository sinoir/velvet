package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureSummary;
import com.delectable.mobile.model.api.accounts.AccountContextResponse;

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
        AccountContextResponse responseObject = mGson.fromJson(json.toString(),
                AccountContextResponse.class);
        Account actualAccount = responseObject.payload.account;

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

        AccountContextResponse responseObject = mGson.fromJson(json.toString(),
                AccountContextResponse.class);
        Account actualAccount = responseObject.payload.account;

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

        assertEquals("jNHflR9U9xRQjA", actualAccount.getETag());
        assertEquals(-1, actualAccount.getCurrentUserRelationship().intValue());
        assertEquals(0, actualAccount.getCaptureSummaries().size());
    }

    public void testParseAccountsOtherProfileCtx() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_other_profile_ctx);
        AccountContextResponse responseObject = mGson.fromJson(json.toString(),
                AccountContextResponse.class);
        Account actualAccount = responseObject.payload.account;

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

    public void testGetFullName() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_accounts_other_profile_ctx);
        AccountContextResponse responseObject = mGson.fromJson(json.toString(),
                AccountContextResponse.class);
        Account actualAccount = responseObject.payload.account;

        String expectedName = "James Wooldridge";
        assertEquals(expectedName, actualAccount.getFullName());
    }

    public void testUserRelationshipTypeSelf() {
        Account actualAccount = new Account();
        actualAccount.setCurrentUserRelationship(Account.RELATION_TYPE_SELF);

        assertTrue(actualAccount.isUserRelationshipTypeSelf());
        assertFalse(actualAccount.isUserRelationshipTypeNone());
        assertFalse(actualAccount.isUserRelationshipTypeFollowing());
    }

    public void testUserRelationshipTypeFollowing() {
        Account actualAccount = new Account();
        actualAccount.setCurrentUserRelationship(Account.RELATION_TYPE_FOLLOWING);

        assertFalse(actualAccount.isUserRelationshipTypeSelf());
        assertFalse(actualAccount.isUserRelationshipTypeNone());
        assertTrue(actualAccount.isUserRelationshipTypeFollowing());
    }

    public void testUserRelationshipTypeNone() {
        Account actualAccount = new Account();
        actualAccount.setCurrentUserRelationship(Account.RELATION_TYPE_NONE);

        assertFalse(actualAccount.isUserRelationshipTypeSelf());
        assertTrue(actualAccount.isUserRelationshipTypeNone());
        assertFalse(actualAccount.isUserRelationshipTypeFollowing());
    }

}
