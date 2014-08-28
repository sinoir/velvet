package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.LocalNotifications;
import com.delectable.mobile.api.models.Registration;
import com.delectable.mobile.model.api.registrations.RegistrationLoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationTest extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParsePayloadForAction() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_registration_success_response);

        RegistrationLoginResponse actualRegistration = mGson.fromJson(json.toString(),
                RegistrationLoginResponse.class);
        Account actualAccount = actualRegistration.payload.account;

        assertEquals("537e2f09753490201d00084f", actualRegistration.payload.session_key);
        assertEquals("OwcHeVvNMQ", actualRegistration.payload.session_token);

        AccountConfig actualAConfig = actualAccount.getAccountConfig();
        Identifier actualIdentifier = actualAccount.getIdentifiers().get(0);
        LocalNotifications actualNotif = actualAccount.getLocalNotifs();

        assertEquals("537e2f09753490201d00084e", actualAccount.getId());
        assertEquals("Adam", actualAccount.getFname());
        assertEquals("Bednarek", actualAccount.getLname());
        assertFalse(actualAccount.isInfluencer());
        assertEquals("", actualAccount.getInfluencerTitles().get(0));
        assertEquals("", actualAccount.getBio());
        assertEquals(0, actualAccount.getFollowerCount().intValue());
        assertEquals(0, actualAccount.getFollowerCount().intValue());
        assertEquals(0, actualAccount.getCaptureCount().intValue());
        assertEquals(0, actualAccount.getRegionCount().intValue());
        assertEquals(0, actualAccount.getWishlistCount().intValue());
        assertEquals("", actualAccount.getUrl());
        assertEquals("https://s3.amazonaws.com/delectableStockPhotos/no_photo.png",
                actualAccount.getPhoto().getUrl());
        assertEquals(false, actualAConfig.getPassiveOgSharing().booleanValue());
        assertEquals(false, actualAConfig.getPassiveVintankSharing().booleanValue());
        assertEquals(true, actualAConfig.getPnCaptureTranscribed().booleanValue());
        assertEquals(true, actualAConfig.getPnCommentOnOwnWine().booleanValue());
        assertEquals(true, actualAConfig.getPnCommentResponse().booleanValue());
        assertEquals(true, actualAConfig.getPnExperiment().booleanValue());
        assertEquals(true, actualAConfig.getPnFriendJoined().booleanValue());
        assertEquals(true, actualAConfig.getPnLikeOnOwnWine().booleanValue());
        assertEquals(true, actualAConfig.getPnNewFollower().booleanValue());
        assertEquals(true, actualAConfig.getPnPurchaseOfferMade().booleanValue());
        assertEquals(true, actualAConfig.getPnTagged().booleanValue());
        assertEquals(0, actualAConfig.getTaggingTest().intValue());
        assertEquals("email", actualIdentifier.getType());
        assertEquals("537e2f09753490201d000851", actualIdentifier.getId());
        assertEquals("adam@ad60.com", actualIdentifier.getString());
        assertEquals(false, actualIdentifier.getVerified().booleanValue());
        assertEquals(true, actualIdentifier.getPrimary().booleanValue());

        assertEquals(0, actualAccount.getShippingAddresses().size());
        assertEquals(0, actualAccount.getPaymentMethods().size());
        assertEquals("adam@ad60.com", actualAccount.getEmail());
        // TODO: custom parse these
        assertEquals(0, actualAccount.getActivityFeedTsLast());
        assertEquals(false, actualAccount.getFtueCompleted());
        assertEquals(true, actualNotif.getSendLnOne().booleanValue());
        assertEquals(true, actualNotif.getSendLnTwo().booleanValue());
        assertEquals(false, actualNotif.getSendLnThree().booleanValue());
        assertEquals(false, actualNotif.getSendLnFour().booleanValue());
        assertEquals(false, actualNotif.getSendLnFive().booleanValue());
    }

    private Registration buildTestModel() {
        Registration regModel = new Registration();
        // TODO: Replace with some Account info?
        regModel.setAccount(new Account());
        regModel.setSessionType("Some Session Type");
        regModel.setSessionToken("Some Session Token");
        regModel.setSessionKey("Some Session Key");

        return regModel;
    }
}
