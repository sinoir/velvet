package com.delectable.mobile.tests;

import com.delectable.mobile.api.endpointmodels.registrations.RegisterLoginResponse;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.LocalNotifications;

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

        RegisterLoginResponse actualRegistration = mGson.fromJson(json.toString(),
                RegisterLoginResponse.class);
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
        assertEquals(0, actualAccount.getFollowerCount());
        assertEquals(0, actualAccount.getFollowerCount());
        assertEquals(0, actualAccount.getCaptureCount());
        assertEquals(0, actualAccount.getRegionCount());
        assertEquals(0, actualAccount.getWishlistCount());
        assertEquals("", actualAccount.getUrl());
        assertEquals("https://s3.amazonaws.com/delectableStockPhotos/no_photo.png",
                actualAccount.getPhoto().getUrl());
        assertEquals(false, actualAConfig.getPassiveOgSharing());
        assertEquals(false, actualAConfig.getPassiveVintankSharing());
        assertEquals(true, actualAConfig.getPnCaptureTranscribed());
        assertEquals(true, actualAConfig.getPnCommentOnOwnWine());
        assertEquals(true, actualAConfig.getPnCommentResponse());
        assertEquals(true, actualAConfig.getPnExperiment());
        assertEquals(true, actualAConfig.getPnFriendJoined());
        assertEquals(true, actualAConfig.getPnLikeOnOwnWine());
        assertEquals(true, actualAConfig.getPnNewFollower());
        assertEquals(true, actualAConfig.getPnPurchaseOfferMade());
        assertEquals(true, actualAConfig.getPnTagged());
        assertEquals(0, actualAConfig.getTaggingTest());
        assertEquals("email", actualIdentifier.getType());
        assertEquals("537e2f09753490201d000851", actualIdentifier.getId());
        assertEquals("adam@ad60.com", actualIdentifier.getString());
        assertEquals(false, actualIdentifier.getVerified());
        assertEquals(true, actualIdentifier.getPrimary());

        assertEquals(0, actualAccount.getShippingAddresses().size());
        assertEquals(0, actualAccount.getPaymentMethods().size());
        assertEquals("adam@ad60.com", actualAccount.getEmail());
        // TODO: custom parse these
        assertEquals(0.0f, actualAccount.getActivityFeedTsLast());
        assertEquals(false, actualAccount.getFtueCompleted());
        assertEquals(true, actualNotif.getSendLnOne());
        assertEquals(true, actualNotif.getSendLnTwo());
        assertEquals(false, actualNotif.getSendLnThree());
        assertEquals(false, actualNotif.getSendLnFour());
        assertEquals(false, actualNotif.getSendLnFive());
    }
}
