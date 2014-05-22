package com.delectable.mobile.data;

import com.delectable.mobile.api.models.Registration;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abednarek on 5/22/14.
 */
public class UserInfo {

    public static final String PREFERENCES = "com.delectable.mobile.data.userinfo";

    private static final String PROPERTY_SESSION_TOKEN = "sessionToken";

    private static final String PROPERTY_SESSION_KEY = "sessionKey";


    public static void onSignIn(Context context, Registration newRegistration) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_SESSION_KEY, newRegistration.getSessionKey());
        editor.putString(PROPERTY_SESSION_TOKEN, newRegistration.getSessionToken());
        editor.commit();
    }

    public static void onSignOut(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_SESSION_KEY);
        editor.remove(PROPERTY_SESSION_TOKEN);
        editor.commit();
    }

    public static boolean isSignedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getAll().size() > 0;
    }

    public static String getSessionToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_SESSION_TOKEN, null);
    }

    public static String getSessionKey(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_SESSION_KEY, null);
    }
}
