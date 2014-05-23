package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.LocalNotifications;
import com.delectable.mobile.api.models.Registration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abednarek on 5/21/14.
 */
public class RegistrationTest extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPayloadFieldsForRegisterAction() {
        Registration regModel = new Registration();
        String[] actualFields = regModel.getPayloadFieldsForAction(Registration.A_REGISTER);
        String[] expectedFields = new String[]{
                "session_type",
                "email",
                "password",
                "fname",
                "lname",
        };
        for (int i = 0; i < expectedFields.length; i++) {
            assertEquals(expectedFields[i], actualFields[i]);
        }
    }

    public void testGetPayloadFieldsForLoginAction() {
        Registration regModel = new Registration();
        String[] actualFields = regModel.getPayloadFieldsForAction(Registration.A_LOGIN);
        String[] expectedFields = new String[]{
                "session_type",
                "email",
                "password",
        };
        for (int i = 0; i < expectedFields.length; i++) {
            assertEquals(expectedFields[i], actualFields[i]);
        }
    }

    public void testGetPayloadFieldsForFacebookAction() {
        Registration regModel = new Registration();
        String[] actualFields = regModel.getPayloadFieldsForAction(Registration.A_FACEBOOK);
        String[] expectedFields = new String[]{
                "session_type",
                "facebook_token",
                "facebook_token_expiration",
        };
        for (int i = 0; i < expectedFields.length; i++) {
            assertEquals(expectedFields[i], actualFields[i]);
        }
    }

    public void testActionApiPaths() {
        Registration regModel = new Registration();
        String basePath = "/v2/registrations/";
        assertEquals(basePath + "register",
                regModel.getResourceUrlForAction(Registration.A_REGISTER));
        assertEquals(basePath + "login", regModel.getResourceUrlForAction(Registration.A_LOGIN));
        assertEquals(basePath + "facebook",
                regModel.getResourceUrlForAction(Registration.A_FACEBOOK));
    }

    public void testBuildPayloadMapForRegistrationAction() {
        Registration regModel = buildTestModel();

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("session_type", regModel.getSessionType());
        expectedMap.put("email", regModel.getEmail());
        expectedMap.put("password", regModel.getPassword());
        expectedMap.put("fname", regModel.getFname());
        expectedMap.put("lname", regModel.getLname());

        Map<String, String> actualMap = regModel.buildPayloadMapForAction(regModel.A_REGISTER);
        assertEquals(expectedMap, actualMap);
    }

    public void testBuildPayloadMapFoLoginAction() {
        Registration regModel = buildTestModel();

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("session_type", regModel.getSessionType());
        expectedMap.put("email", regModel.getEmail());
        expectedMap.put("password", regModel.getPassword());

        Map<String, String> actualMap = regModel.buildPayloadMapForAction(regModel.A_LOGIN);
        assertEquals(expectedMap, actualMap);
    }

    public void testBuildPayloadMapForFacebookAction() {
        Registration regModel = buildTestModel();

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("session_type", regModel.getSessionType());
        expectedMap.put("facebook_token", regModel.getFacebookToken());
        expectedMap.put("facebook_token_expiration",
                regModel.getFacebookTokenExpiration().toString());

        Map<String, String> actualMap = regModel.buildPayloadMapForAction(regModel.A_FACEBOOK);
        assertEquals(expectedMap, actualMap);
    }

    public void testParsePayloadForAction() throws JSONException {
        Registration someRegistration = buildTestModel();
        JSONObject json = loadJsonObjectFromResource(R.raw.test_registration_success_response);

        // Action is ignored for registration for now
        Registration actualRegistration = someRegistration
                .parsePayloadForAction(json, -1);
        assertEquals("537e2f09753490201d00084f", actualRegistration.getSessionKey());
        assertEquals("OwcHeVvNMQ", actualRegistration.getSessionToken());

        Account actualAccount = actualRegistration.getAccount();
        AccountConfig actualAConfig = actualAccount.getAccountConfig();
        Identifier actualIdentifier = actualAccount.getIdentifiers().get(0);
        LocalNotifications actualNotif = actualAccount.getLocalNotifs();

        assertEquals("537e2f09753490201d00084e", actualAccount.getId());
        assertEquals("Adam", actualAccount.getFname());
        assertEquals("Bednarek", actualAccount.getLname());
        assertFalse(actualAccount.getInfluencer());
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
        assertEquals(0, actualAccount.getActivityFeedTsLast().intValue());
        assertEquals(false, actualAccount.getFtueCompleted().booleanValue());
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
        regModel.setEmail("Some Email");
        regModel.setFacebookToken("Some Facebook Token");
        regModel.setFacebookTokenExpiration(123.456);
        regModel.setFname("Some FName");
        regModel.setLname("Some LName");
        regModel.setPassword("Some Password");
        regModel.setSessionType("Some Session Type");
        regModel.setSessionToken("Some Session Token");
        regModel.setSessionKey("Some Session Key");

        return regModel;
    }
}
