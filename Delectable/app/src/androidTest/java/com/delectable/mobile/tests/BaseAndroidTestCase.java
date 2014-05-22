package com.delectable.mobile.tests;

import com.delectable.mobile.data.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.AndroidTestCase;

public class BaseAndroidTestCase extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        clearPrefs();
    }

    @Override
    protected void tearDown() throws Exception {
        clearPrefs();
        super.tearDown();
    }

    protected void clearPrefs() {
        SharedPreferences prefs = getContext().getSharedPreferences(UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }
}
