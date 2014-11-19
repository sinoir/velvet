package com.delectable.mobile.tests;

import com.delectable.mobile.api.cache.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoTest extends BaseAndroidTestCase {

    private String mExpectedId;

    private String mExpectedName;

    private String mExpectedEmail;

    private String mExpectedToken;

    private String mExpectedKey;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mExpectedId = "someiD";
        mExpectedName = "johnny boy";
        mExpectedEmail = "some@email.com";
        mExpectedToken = "Session Token";
        mExpectedKey = "Session Key";
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
        UserInfo.onSignIn(mExpectedId, mExpectedName, mExpectedEmail, mExpectedKey, mExpectedToken);
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        assertEquals(mExpectedKey, UserInfo.getSessionKey(getContext()));
        assertEquals(mExpectedToken, UserInfo.getSessionToken(getContext()));
        assertEquals(mExpectedId, UserInfo.getUserId(getContext()));
        assertEquals(mExpectedEmail, UserInfo.getUserEmail(getContext()));
        assertEquals(mExpectedName, UserInfo.getUserName(getContext()));

        assertEquals(5, prefs.getAll().size());
    }

    public void testOnSignOut() {
        UserInfo.onSignIn(mExpectedId, mExpectedName, mExpectedEmail, mExpectedKey, mExpectedToken);
        // Set Over21 to make sure we unset this value on Signout
        UserInfo.setIsOver21(true);
        
        UserInfo.onSignOut(getContext());
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        assertNull(UserInfo.getSessionKey(getContext()));
        assertNull(UserInfo.getSessionToken(getContext()));
        assertFalse(UserInfo.isOver21Set());

        assertEquals(0, prefs.getAll().size());
    }

    public void testIsSignedIn() {
        assertFalse(UserInfo.isSignedIn(getContext()));

        UserInfo.onSignIn(mExpectedId, mExpectedName, mExpectedEmail, mExpectedKey, mExpectedToken);
        assertTrue(UserInfo.isSignedIn(getContext()));

        UserInfo.onSignOut(getContext());
        assertFalse(UserInfo.isSignedIn(getContext()));
    }

    public void testIsOver21Set() {
        assertFalse(UserInfo.isOver21Set());
        UserInfo.setIsOver21(false);
        assertTrue(UserInfo.isOver21Set());
    }
}
