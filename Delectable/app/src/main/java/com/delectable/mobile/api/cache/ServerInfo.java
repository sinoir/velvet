package com.delectable.mobile.api.cache;

import com.delectable.mobile.App;

import android.content.Context;
import android.content.SharedPreferences;

public class ServerInfo {

    //TODO eventually integrate this into the Environment url?
    public static final String API_VERSION = "/v2";

    public enum Environment {
        PROD("https://api.delectable.com", "http://motd.delectable.com/"),
        MOBILE("http://mobile-api.delectable.com", "http://mobile-motd.delectable.com/"),
        STAGING("https://staging-api.delectable.com", "http://staging-motd.delectable.com/");

        private String mUrl;

        private String mMotdUrl;

        private Environment(String url, String motdUrl) {
            mUrl = url;
            mMotdUrl = motdUrl;
        }

        public String getUrl() {
            return mUrl;
        }

        public String getMotdUrl() {
            return mMotdUrl;
        }
    }

    public static final String PREFERENCES = "com.delectable.mobile.data.serverinfo";

    private static final String DEFAULT_ENVIRONMENT = "defaultEnvironment";

    public static void setEnvironment(Environment env) {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DEFAULT_ENVIRONMENT, env.ordinal());
        editor.commit();
    }

    /**
     * Defaults to {@link Environment#PROD Prod} if the value hasn't been set yet.
     */
    public static Environment getEnvironment() {
        SharedPreferences prefs = App.getInstance().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
        int position = prefs.getInt(DEFAULT_ENVIRONMENT, Environment.PROD.ordinal());
        return Environment.values()[position];
    }

    public static void onSignOut(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(DEFAULT_ENVIRONMENT);
        editor.commit();
    }
}
