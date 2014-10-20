package com.delectable.mobile.util;

import com.crashlytics.android.Crashlytics;
import com.delectable.mobile.BuildConfig;

public class CrashlyticsUtil {

    private static final String SESSION_KEY = "SESSION_KEY";

    public static void onSignIn(String name, String email, String userId, String sessionKey) {
        if (Crashlytics.getInstance() != null) {
            Crashlytics.setUserName(name);
            Crashlytics.setUserEmail(email);
            Crashlytics.setUserIdentifier(userId);
            Crashlytics.setString(SESSION_KEY, sessionKey);
        }
    }

    public static void onSignOut() {
        if (Crashlytics.getInstance() != null) {
            Crashlytics.setUserName(null);
            Crashlytics.setUserEmail(null);
            Crashlytics.setUserIdentifier(null);
            Crashlytics.setString(SESSION_KEY, null);
        }
    }

    public static void log(int priority, String tag, String message) {
        Crashlytics.log(priority, tag, message);
    }

    public static void log(String message) {
        if (BuildConfig.REPORT_CRASHES) {
            Crashlytics.log(message);
        }
    }

}
