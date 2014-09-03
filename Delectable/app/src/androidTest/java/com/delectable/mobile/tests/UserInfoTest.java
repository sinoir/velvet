package com.delectable.mobile.tests;

import com.delectable.mobile.data.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoTest extends BaseAndroidTestCase {

    private String mExpectedToken;

    private String mExpectedKey;

    private String mExpectedId;

    private String mExpectedEmail;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mExpectedToken = "Session Token";
        mExpectedKey = "Session Key";
        mExpectedId = "someiD";
        mExpectedEmail = "some@email.com";
    }

    @Override
    protected void tearDown() throws Exception {
        mExpectedToken = null;
        mExpectedKey = null;
        mExpectedId = null;
        mExpectedEmail = null;
        super.tearDown();
    }


    public void testOnSignIn() {
        UserInfo.onSignIn(mExpectedId, mExpectedKey, mExpectedToken, mExpectedEmail);
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        assertEquals(mExpectedKey, UserInfo.getSessionKey(getContext()));
        assertEquals(mExpectedToken, UserInfo.getSessionToken(getContext()));
        assertEquals(mExpectedId, UserInfo.getUserId(getContext()));
        assertEquals(mExpectedEmail, UserInfo.getUserEmail(getContext()));

        assertEquals(4, prefs.getAll().size());
    }

    public void testOnSignOut() {
        UserInfo.onSignIn(mExpectedId, mExpectedKey, mExpectedToken, mExpectedEmail);
        UserInfo.onSignOut(getContext());
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        assertNull(UserInfo.getSessionKey(getContext()));
        assertNull(UserInfo.getSessionToken(getContext()));

        assertEquals(0, prefs.getAll().size());
    }

    public void testIsSignedIn() {
        assertFalse(UserInfo.isSignedIn(getContext()));

        UserInfo.onSignIn(mExpectedId, mExpectedKey, mExpectedToken, mExpectedEmail);
        assertTrue(UserInfo.isSignedIn(getContext()));

        UserInfo.onSignOut(getContext());
        assertFalse(UserInfo.isSignedIn(getContext()));
    }
}
