package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.requests.AccountsContextRequest;

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

}
