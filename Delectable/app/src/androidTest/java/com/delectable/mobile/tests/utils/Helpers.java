package com.delectable.mobile.tests.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Common Helpers for Tests
 */
public class Helpers {

    public static String readFileAsString(Context testContext, int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        String resultString;
        Resources testRes = testContext.getResources();
        InputStream inputStream = testRes.openRawResource(resourceId);
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                inputStream, Charset.forName("UTF-8")));
        try {
            while ((line = bufReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            resultString = stringBuilder.toString();
            bufReader.close();
            inputStream.close();
        } catch (IOException e) {
            resultString = null;
        }
        return resultString;
    }

    public static JSONObject jsonObjectFromResource(Context testContext,
            int resourceId) throws JSONException {
        JSONObject jsonObject = new JSONObject(readFileAsString(testContext,
                resourceId));
        return jsonObject;
    }

    public static JSONArray jsonArrayFromResource(Context testContext,
            int resourceId) throws JSONException {
        JSONArray jsonArray = new JSONArray(readFileAsString(testContext,
                resourceId));
        return jsonArray;
    }
}
