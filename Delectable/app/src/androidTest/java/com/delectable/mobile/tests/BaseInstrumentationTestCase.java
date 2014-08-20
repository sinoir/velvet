package com.delectable.mobile.tests;

import com.google.gson.Gson;

import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.tests.utils.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.InstrumentationTestCase;

public class BaseInstrumentationTestCase extends InstrumentationTestCase {

    Gson mGson = new Gson();

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
        SharedPreferences prefs = getInstrumentation().getTargetContext().getSharedPreferences(
                UserInfo.PREFERENCES,
                Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    protected JSONObject loadJsonObjectFromResource(int resourceId) throws JSONException {
        return Helpers.jsonObjectFromResource(getInstrumentation().getContext(), resourceId);
    }

    protected JSONArray loadJsonArrayFromResource(int resourceId) throws JSONException {
        return Helpers.jsonArrayFromResource(getInstrumentation().getContext(), resourceId);
    }
}
