package com.delectable.mobile.api.cache;

import com.google.gson.Gson;

import com.delectable.mobile.App;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.Motd;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {

    public static final String PREFERENCES = "com.delectable.mobile.data.userinfo";

    private static final String PROPERTY_SESSION_TOKEN = "sessionToken";

    private static final String PROPERTY_SESSION_KEY = "sessionKey";

    private static final String PROPERTY_USER_ID = "userId";

    private static final String PROPERTY_USER_NAME = "userName";

    private static final String PROPERTY_USER_EMAIL = "userEmail";

    private static final String PROPERTY_MOTD = "motd";

    private static final String PROPERTY_ACCOUNT_PRIVATE = "accountPrivate";

    private static final String PROPERTY_ACCOUNT_PRIVATE_TEMP = "accountPrivateTemp";

    private static final String PROPERTY_WELCOME_DONE = "welcomeDone";


    public static void onSignIn(String userId, String fullName, String email, String sessionKey, String sessionToken) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER_ID, userId);
        editor.putString(PROPERTY_USER_NAME, fullName);
        editor.putString(PROPERTY_USER_EMAIL, email);
        editor.putString(PROPERTY_SESSION_KEY, sessionKey);
        editor.putString(PROPERTY_SESSION_TOKEN, sessionToken);

        editor.commit();
    }

    public static void onSignOut(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_USER_ID);
        editor.remove(PROPERTY_USER_NAME);
        editor.remove(PROPERTY_USER_EMAIL);
        editor.remove(PROPERTY_SESSION_KEY);
        editor.remove(PROPERTY_SESSION_TOKEN);
        editor.remove(PROPERTY_MOTD);
        editor.remove(PROPERTY_ACCOUNT_PRIVATE);
        editor.commit();
    }

    public static void setMotd(Motd motd) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonString = gson.toJson(motd);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_MOTD, jsonString);
        editor.commit();
    }

    public static void setAccountPrivate(Account account) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonString = gson.toJson(account);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_ACCOUNT_PRIVATE, jsonString);
        editor.commit();
    }

    public static boolean isSignedIn(Context context) {
        return getSessionToken(context) != null;
    }

    public static String getSessionToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_SESSION_TOKEN, null);
    }

    public static String getSessionKey(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_SESSION_KEY, null);
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_USER_ID, null);
    }

    public static boolean isLoggedInUser(Context context, String userId) {
        return userId != null && userId.equals(getUserId(context));
    }

        public static String getUserName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String name = prefs.getString(PROPERTY_USER_NAME, null);
        //this check is necessary this method was created after 1.0 release, their user name might not have been
        //saved upon signin.
        if (name == null) {
            Account account = getAccountPrivate(context);
            if (account != null) {
                name = account.getFullName();

                //write back into shared prefs
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PROPERTY_USER_NAME, name);
                editor.commit();
            }

        }
        return name;
    }

    public static String getUserEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(PROPERTY_USER_EMAIL, null);
    }

    public static Motd getMotd(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(PROPERTY_MOTD, null);
        if (jsonString != null) {
            Gson gson = new Gson();
            Motd motd = gson.fromJson(jsonString, Motd.class);
            return motd;
        }
        return null;
    }

    public static Account getAccountPrivate(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(PROPERTY_ACCOUNT_PRIVATE, null);
        if (jsonString != null) {
            Gson gson = new Gson();
            Account account = gson.fromJson(jsonString, Account.class);
            return account;
        }
        return null;
    }


    /**
     * TODO these two methods are not well implemented. The primary purpose is really just to
     * provide a place to persist the account object's original state while it's awaiting updates
     * from an endpoint. If said request returns in error, then we would roll back the real Account
     * private object back to this original state. Specifically, I had a very hard time
     * holding onto member objects in Jobs that had persist() called on them.
     */
    public static void setTempAccount(Account account) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonString = gson.toJson(account);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_ACCOUNT_PRIVATE_TEMP, jsonString);
        editor.commit();
    }

    public static Account getTempAccountPrivate() {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(PROPERTY_ACCOUNT_PRIVATE_TEMP, null);
        if (jsonString != null) {
            Gson gson = new Gson();
            Account account = gson.fromJson(jsonString, Account.class);
            return account;
        }
        return null;
    }

    public static void clearTempAccount() {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_ACCOUNT_PRIVATE_TEMP);
        editor.commit();
    }

    public static boolean isWelcomeDone() {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(PROPERTY_WELCOME_DONE, false);
    }

    public static void markWelcomeDone() {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PROPERTY_WELCOME_DONE, true);
        editor.commit();
    }

}
