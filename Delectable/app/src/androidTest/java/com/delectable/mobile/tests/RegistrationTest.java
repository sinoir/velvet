package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.Identifier;
import com.delectable.mobile.api.models.LocalNotifications;
import com.delectable.mobile.api.models.Registration;
import com.delectable.mobile.api.requests.RegistrationsFacebook;
import com.delectable.mobile.api.requests.RegistrationsLogin;
import com.delectable.mobile.api.requests.RegistrationsRegister;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        RegistrationsRegister request = new RegistrationsRegister();
        String[] actualFields = request.getPayloadFields();
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
        RegistrationsLogin request = new RegistrationsLogin();
        String[] actualFields = request.getPayloadFields();
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
        RegistrationsFacebook request = new RegistrationsFacebook();
        String[] actualFields = request.getPayloadFields();
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
        RegistrationsRegister regRequest = new RegistrationsRegister();
        RegistrationsLogin loginRequest = new RegistrationsLogin();
        RegistrationsFacebook fbRequest = new RegistrationsFacebook();

        String basePath = "/v2/registrations/";
        assertEquals(basePath + "register", regRequest.getResourceUrl());
        assertEquals(basePath + "login", loginRequest.getResourceUrl());
        assertEquals(basePath + "facebook", fbRequest.getResourceUrl());
    }

    public void testBuildPayloadMapForRegistrationAction() {
        RegistrationsRegister request = new RegistrationsRegister();
        request.setEmail("Some Email");
        request.setPassword("Some Password");
        request.setSessionType("Some Session");
        request.setFname("First Name");
        request.setLname("Last Name");

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("session_type", request.getSessionType());
        expectedMap.put("email", request.getEmail());
        expectedMap.put("password", request.getPassword());
        expectedMap.put("fname", request.getFname());
        expectedMap.put("lname", request.getLname());

        Map<String, String> actualMap = request.buildPayloadMap();
        assertEquals(expectedMap, actualMap);
    }

    public void testBuildPayloadMapFoLoginAction() {
        RegistrationsLogin request = new RegistrationsLogin();
        request.setEmail("Some Email");
        request.setPassword("Some Password");
        request.setSessionType("Some Session");

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("session_type", request.getSessionType());
        expectedMap.put("email", request.getEmail());
        expectedMap.put("password", request.getPassword());

        Map<String, String> actualMap = request.buildPayloadMap();
        assertEquals(expectedMap, actualMap);
    }

    public void testBuildPayloadMapForFacebookAction() {
        RegistrationsFacebook request = new RegistrationsFacebook();
        request.setFacebookToken("Some Token");
        request.setFacebookTokenExpiration(1234.034);
        request.setSessionType("Some Session");

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("session_type", request.getSessionType());
        expectedMap.put("facebook_token", request.getFacebookToken());
        expectedMap.put("facebook_token_expiration",
                request.getFacebookTokenExpiration().toString());

        Map<String, String> actualMap = request.buildPayloadMap();
        assertEquals(expectedMap, actualMap);
    }

    public void testParsePayloadForAction() throws JSONException {
        Registration someRegistration = buildTestModel();
        JSONObject json = loadJsonObjectFromResource(R.raw.test_registration_success_response);

        Registration actualRegistration = (Registration) someRegistration.buildFromJson(json);
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
