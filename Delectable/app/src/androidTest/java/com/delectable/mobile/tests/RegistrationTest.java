package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.Registration;

import junit.framework.TestCase;

/**
 * Created by abednarek on 5/21/14.
 */
public class RegistrationTest extends TestCase {

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

}
